package org.almacen.model;

// Generated 08-may-2015 17:42:17 by Hibernate Tools 4.3.1

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Tipomovimiento generated by hbm2java
 */
@Entity
@Table(name = "tipomovimiento"
      , catalog = "bdalmacen")
public class Tipomovimiento implements java.io.Serializable
{

   private Integer idtipomovimiento;
   private String tipomovimiento;
   private Integer entradasalida;
   private String descripcion;
   private String fecharegistro;
   private String fechamodificacion;
   private Integer idestado;

   public Tipomovimiento()
   {
   }

   public Tipomovimiento(String tipomovimiento, Integer entradasalida, String descripcion, String fecharegistro, String fechamodificacion, Integer idestado)
   {
      this.tipomovimiento = tipomovimiento;
      this.entradasalida = entradasalida;
      this.descripcion = descripcion;
      this.fecharegistro = fecharegistro;
      this.fechamodificacion = fechamodificacion;
      this.idestado = idestado;
   }

   @Id
   @GeneratedValue(strategy = IDENTITY)
   @Column(name = "idtipomovimiento", unique = true, nullable = false)
   public Integer getIdtipomovimiento()
   {
      return this.idtipomovimiento;
   }

   public void setIdtipomovimiento(Integer idtipomovimiento)
   {
      this.idtipomovimiento = idtipomovimiento;
   }

   @Column(name = "tipomovimiento", length = 50)
   public String getTipomovimiento()
   {
      return this.tipomovimiento;
   }

   public void setTipomovimiento(String tipomovimiento)
   {
      this.tipomovimiento = tipomovimiento;
   }

   @Column(name = "entradasalida")
   public Integer getEntradasalida()
   {
      return this.entradasalida;
   }

   public void setEntradasalida(Integer entradasalida)
   {
      this.entradasalida = entradasalida;
   }

   @Column(name = "descripcion", length = 250)
   public String getDescripcion()
   {
      return this.descripcion;
   }

   public void setDescripcion(String descripcion)
   {
      this.descripcion = descripcion;
   }

   @Column(name = "fecharegistro", length = 20)
   public String getFecharegistro()
   {
      return this.fecharegistro;
   }

   public void setFecharegistro(String fecharegistro)
   {
      this.fecharegistro = fecharegistro;
   }

   @Column(name = "fechamodificacion", length = 20)
   public String getFechamodificacion()
   {
      return this.fechamodificacion;
   }

   public void setFechamodificacion(String fechamodificacion)
   {
      this.fechamodificacion = fechamodificacion;
   }

   @Column(name = "idestado")
   public Integer getIdestado()
   {
      return this.idestado;
   }

   public void setIdestado(Integer idestado)
   {
      this.idestado = idestado;
   }

}
