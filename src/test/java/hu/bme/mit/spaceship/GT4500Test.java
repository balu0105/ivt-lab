package hu.bme.mit.spaceship;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class GT4500Test {

  private GT4500 ship;

  private TorpedoStore mockPrimaryTorpedoStore;
  private TorpedoStore mockSecondaryTorpedoStore;

  @BeforeEach
  public void init(){
    mockPrimaryTorpedoStore = mock(TorpedoStore.class);
    mockSecondaryTorpedoStore = mock(TorpedoStore.class);

    this.ship = new GT4500(mockPrimaryTorpedoStore, mockSecondaryTorpedoStore);
  }

  @Test
  public void fireTorpedo_Single_Success(){
    /*// Arrange

    // Act
    boolean result = ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    assertEquals(true, result);*/
    //Arrange
    when(mockPrimaryTorpedoStore.fire(1)).thenReturn(true);
    when(mockPrimaryTorpedoStore.isEmpty()).thenReturn(false);

    // Act
    boolean result = ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    assertEquals(true, result);

    verify(mockPrimaryTorpedoStore, times(1)).fire(1);
    verify(mockPrimaryTorpedoStore, times(1)).isEmpty();
  }

  @Test
  public void fireTorpedo_All_Success(){
    /*// Arrange

    // Act
    boolean result = ship.fireTorpedo(FiringMode.ALL);

    // Assert
    assertEquals(true, result);*/

    // Arrange
    when(mockPrimaryTorpedoStore.fire(1)).thenReturn(true);
    when(mockPrimaryTorpedoStore.isEmpty()).thenReturn(false);
    when(mockSecondaryTorpedoStore.fire(1)).thenReturn(true);
    when(mockSecondaryTorpedoStore.isEmpty()).thenReturn(false);

    // Act
    boolean result = ship.fireTorpedo(FiringMode.ALL);

    // Assert
    assertEquals(true, result);

    verify(mockPrimaryTorpedoStore, times(1)).fire(1);
    verify(mockSecondaryTorpedoStore, times(1)).fire(1);
    verify(mockPrimaryTorpedoStore, times(1)).isEmpty();
    verify(mockSecondaryTorpedoStore, times(1)).isEmpty();
  }

  @Test
  public void fireLaserNotImplemented(){
    boolean result = ship.fireLaser(FiringMode.ALL);

    assertEquals(false, result);
  }

  @Test
  public void Single_PrimaryFiredLast_SecondaryHasTorpedo(){
    ship.setWasPrimaryFiredLast(true);

    when(mockSecondaryTorpedoStore.fire(1)).thenReturn(true);
    when(mockSecondaryTorpedoStore.isEmpty()).thenReturn(false);

    boolean result = ship.fireTorpedo(FiringMode.SINGLE);

    assertEquals(true, result);

    verify(mockSecondaryTorpedoStore, times(1)).fire(1);
    verify(mockSecondaryTorpedoStore, times(1)).isEmpty();
  }

  @Test
  public void Single_NotPrimaryFiredLast_PrimaryEmpty_SecondaryHasTorpedo(){
    when(mockPrimaryTorpedoStore.isEmpty()).thenReturn(true);
    when(mockSecondaryTorpedoStore.fire(1)).thenReturn(true);
    when(mockSecondaryTorpedoStore.isEmpty()).thenReturn(false);

    boolean result = ship.fireTorpedo(FiringMode.SINGLE);

    assertEquals(true, result);

    verify(mockPrimaryTorpedoStore, times(1)).isEmpty();
    verify(mockPrimaryTorpedoStore, never()).fire(1);
    verify(mockSecondaryTorpedoStore, times(1)).fire(1);
    verify(mockSecondaryTorpedoStore, times(1)).isEmpty();
  }

  @Test void All_BothEmpty(){
    when(mockPrimaryTorpedoStore.isEmpty()).thenReturn(true);
    when(mockSecondaryTorpedoStore.isEmpty()).thenReturn(true);

    boolean result = ship.fireTorpedo(FiringMode.ALL);

    assertEquals(false, result);

    verify(mockPrimaryTorpedoStore, times(1)).isEmpty();
    verify(mockPrimaryTorpedoStore, never()).fire(1);
    verify(mockSecondaryTorpedoStore, never()).fire(1);
    verify(mockSecondaryTorpedoStore, times(1)).isEmpty();
  }

  @Test void All_PrimaryEmpty_SecondaryHasTorpedo(){
    when(mockSecondaryTorpedoStore.isEmpty()).thenReturn(false);
    when(mockSecondaryTorpedoStore.fire(1)).thenReturn(true);
    when(mockPrimaryTorpedoStore.isEmpty()).thenReturn(true);

    boolean result = ship.fireTorpedo(FiringMode.ALL);

    assertEquals(true, result);

    verify(mockPrimaryTorpedoStore, times(1)).isEmpty();
    verify(mockPrimaryTorpedoStore, never()).fire(1);
    verify(mockSecondaryTorpedoStore, times(1)).fire(1);
    verify(mockSecondaryTorpedoStore, times(1)).isEmpty();
  }

  @Test void All_SecondaryEmpty_PrimaryHasTorpedo(){
    when(mockSecondaryTorpedoStore.isEmpty()).thenReturn(true);
    when(mockPrimaryTorpedoStore.fire(1)).thenReturn(true);
    when(mockPrimaryTorpedoStore.isEmpty()).thenReturn(false);

    boolean result = ship.fireTorpedo(FiringMode.ALL);

    assertEquals(true, result);

    verify(mockPrimaryTorpedoStore, times(1)).isEmpty();
    verify(mockPrimaryTorpedoStore, times(1)).fire(1);
    verify(mockSecondaryTorpedoStore, never()).fire(1);
    verify(mockSecondaryTorpedoStore, times(1)).isEmpty();
  }

  @Test
  public void Single_PrimaryFiredLast_SecondaryEmpty(){
    ship.setWasPrimaryFiredLast(true);

    when(mockPrimaryTorpedoStore.isEmpty()).thenReturn(false);
    when(mockPrimaryTorpedoStore.fire(1)).thenReturn(true);
    when(mockSecondaryTorpedoStore.isEmpty()).thenReturn(true);

    boolean result = ship.fireTorpedo(FiringMode.SINGLE);

    assertEquals(true, result);

    verify(mockPrimaryTorpedoStore, times(1)).fire(1);
    verify(mockPrimaryTorpedoStore, times(1)).isEmpty();
    verify(mockSecondaryTorpedoStore, never()).fire(1);
    verify(mockSecondaryTorpedoStore, times(1)).isEmpty();
  }

  @Test
  public void Single_PrimaryFiredLast_BothEmpty(){
    ship.setWasPrimaryFiredLast(true);

    when(mockPrimaryTorpedoStore.isEmpty()).thenReturn(true);
    when(mockSecondaryTorpedoStore.isEmpty()).thenReturn(true);

    boolean result = ship.fireTorpedo(FiringMode.SINGLE);

    assertEquals(false, result);

    verify(mockPrimaryTorpedoStore, never()).fire(1);
    verify(mockPrimaryTorpedoStore, times(1)).isEmpty();
    verify(mockSecondaryTorpedoStore, never()).fire(1);
    verify(mockSecondaryTorpedoStore, times(1)).isEmpty();
  }

  @Test
  public void Single_NotPrimaryFiredLast_BothEmpty(){
    ship.setWasPrimaryFiredLast(false);

    when(mockPrimaryTorpedoStore.isEmpty()).thenReturn(true);
    when(mockSecondaryTorpedoStore.isEmpty()).thenReturn(true);

    boolean result = ship.fireTorpedo(FiringMode.SINGLE);

    assertEquals(false, result);

    verify(mockPrimaryTorpedoStore, never()).fire(1);
    verify(mockPrimaryTorpedoStore, times(1)).isEmpty();
    verify(mockSecondaryTorpedoStore, never()).fire(1);
    verify(mockSecondaryTorpedoStore, times(1)).isEmpty();
  }

  /*@Test
  public void Default(){
    boolean result = ship.fireTorpedo(null);

    assertEquals(false, result);
  }*/
}
