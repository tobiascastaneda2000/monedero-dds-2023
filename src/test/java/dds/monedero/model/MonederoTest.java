package dds.monedero.model;

import dds.monedero.exceptions.MaximaCantidadDepositosException;
import dds.monedero.exceptions.MaximoExtraccionDiarioException;
import dds.monedero.exceptions.MontoNegativoException;
import dds.monedero.exceptions.SaldoMenorException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MonederoTest {
  private Cuenta cuenta;

  @BeforeEach
  void init() {
    cuenta = new Cuenta();
  }

  //DEPOSITOS
  @Test
  void PonerUnDepositoAumentaSaldo() {
    cuenta.poner(1500);
    assertEquals(cuenta.getSaldo(), 1500);
  }

  @Test
  void PonerDepositoAgregaMovimiento() {
    cuenta.poner(1500);
    cuenta.poner(200);
    assertEquals(cuenta.getMovimientos().size(), 2);
  }

  @Test
  void PonerMontoNegativo() {
    assertThrows(MontoNegativoException.class, () -> cuenta.poner(-1500));
    assertEquals(cuenta.getMovimientos().size(), 0);
  }

  @Test
  void TresDepositos() {
    cuenta.poner(1500);
    cuenta.poner(456);
    cuenta.poner(1900);
    //saldo = 1500 + 456 + 1900 = 3856
    assertEquals(cuenta.getMovimientos().size(), 3);
    assertEquals(cuenta.getSaldo(), 3856);
  }

  @Test
  void MasDeTresDepositos() {
    assertThrows(MaximaCantidadDepositosException.class, () -> {
      cuenta.poner(1500);
      cuenta.poner(456);
      cuenta.poner(1900);
      cuenta.poner(245);
    });
    assertEquals(cuenta.getMovimientos().size(), 3);
    assertEquals(cuenta.getSaldo(), 3856);
  }

  //EXTRACCIONES
  @Test
  void ExtraerMasQueElSaldo() {
    assertThrows(SaldoMenorException.class, () -> {
      cuenta.setSaldo(90);
      cuenta.sacar(1001);
    });
    assertEquals(cuenta.getMovimientos().size(), 0);
  }

  @Test
  public void ExtraerMasDe1000() {
    assertThrows(MaximoExtraccionDiarioException.class, () -> {
      cuenta.setSaldo(5000);
      cuenta.sacar(1001);
    });
    assertEquals(cuenta.getMovimientos().size(), 0);
  }

  @Test
  public void ExtraerMontoNegativo() {
    assertThrows(MontoNegativoException.class, () -> cuenta.sacar(-500));
    assertEquals(cuenta.getMovimientos().size(), 0);
    assertEquals(cuenta.getSaldo(), 0);
  }


  @Test
  public void RealizarUnaExtraccionReduceSaldo() {
    cuenta.setSaldo(2000);
    cuenta.sacar(500);

    /*saldo = 2000 - 500 = 1500*/
    assertEquals(cuenta.getSaldo(), 1500);
  }

  @Test
  public void RealizarExtraccionAgregaMovimiento() {
    cuenta.setSaldo(2000);
    cuenta.sacar(500);
    assertEquals(cuenta.getMovimientos().size(), 1);
  }
}