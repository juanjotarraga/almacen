package org.almacen.model;

// Generated 08-may-2015 17:42:17 by Hibernate Tools 4.3.1

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Almacen generated by hbm2java
 */
@Entity
@Table(name = "almacen"
      , catalog = "bdalmacen")
public class Almacen implements java.io.Serializable
{

   private Integer idalmacen;
   private String almacen;
   private String codigoalmacen;
   private String representante;
   private String observacion;
   private String fecharegistro;
   private String fechamodificacion;
   private Integer idestado;

   public Almacen()
   {
   }

   public Almacen(String almacen, String codigoalmacen, String representante, String observacion)
   {
      this.almacen = almacen;
      this.codigoalmacen = codigoalmacen;
      this.representante = representante;
      this.observacion = observacion;
   }

   public Almacen(String almacen, String codigoalmacen, String representante, String observacion, String fecharegistro, String fechamodificacion, Integer idestado)
   {
      this.almacen = almacen;
      this.codigoalmacen = codigoalmacen;
      this.representante = representante;
      this.observacion = observacion;
      this.fecharegistro = fecharegistro;
      this.fechamodificacion = fechamodificacion;
      this.idestado = idestado;
   }

   @Id
   @GeneratedValue(strategy = IDENTITY)
   @Column(name = "idalmacen", unique = true, nullable = false)
   public Integer getIdalmacen()
   {
      return this.idalmacen;
   }

   public void setIdalmacen(Integer idalmacen)
   {
      this.idalmacen = idalmacen;
   }

   @Column(name = "almacen", nullable = false, length = 50)
   public String getAlmacen()
   {
      return this.almacen;
   }

   public void setAlmacen(String almacen)
   {
      this.almacen = almacen;
   }

   @Column(name = "codigoalmacen", nullable = false, length = 50)
   public String getCodigoalmacen()
   {
      return this.codigoalmacen;
   }

   public void setCodigoalmacen(String codigoalmacen)
   {
      this.codigoalmacen = codigoalmacen;
   }

   @Column(name = "representante", nullable = false, length = 50)
   public String getRepresentante()
   {
      return this.representante;
   }

   public void setRepresentante(String representante)
   {
      this.representante = representante;
   }

   @Column(name = "observacion", nullable = false, length = 250)
   public String getObservacion()
   {
      return this.observacion;
   }

   public void setObservacion(String observacion)
   {
      this.observacion = observacion;
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