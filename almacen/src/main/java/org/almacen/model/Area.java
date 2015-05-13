package org.almacen.model;

// Generated 08-may-2015 17:42:17 by Hibernate Tools 4.3.1

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Area generated by hbm2java
 */
@Entity
@Table(name = "area"
      , catalog = "bdalmacen")
public class Area implements java.io.Serializable
{

   private Integer idarea;
   private String area;
   private String descripcion;
   private String fecharegistro;
   private String fechamodificacion;
   private Integer idcentro;
   private Integer idestado;

   public Area()
   {
   }

   public Area(String area, String descripcion, String fecharegistro, String fechamodificacion)
   {
      this.area = area;
      this.descripcion = descripcion;
      this.fecharegistro = fecharegistro;
      this.fechamodificacion = fechamodificacion;
   }

   public Area(String area, String descripcion, String fecharegistro, String fechamodificacion, Integer idcentro, Integer idestado)
   {
      this.area = area;
      this.descripcion = descripcion;
      this.fecharegistro = fecharegistro;
      this.fechamodificacion = fechamodificacion;
      this.idcentro = idcentro;
      this.idestado = idestado;
   }

   @Id
   @GeneratedValue(strategy = IDENTITY)
   @Column(name = "idarea", unique = true, nullable = false)
   public Integer getIdarea()
   {
      return this.idarea;
   }

   public void setIdarea(Integer idarea)
   {
      this.idarea = idarea;
   }

   @Column(name = "area", nullable = false, length = 50)
   public String getArea()
   {
      return this.area;
   }

   public void setArea(String area)
   {
      this.area = area;
   }

   @Column(name = "descripcion", nullable = false, length = 250)
   public String getDescripcion()
   {
      return this.descripcion;
   }

   public void setDescripcion(String descripcion)
   {
      this.descripcion = descripcion;
   }

   @Column(name = "fecharegistro", nullable = false, length = 20)
   public String getFecharegistro()
   {
      return this.fecharegistro;
   }

   public void setFecharegistro(String fecharegistro)
   {
      this.fecharegistro = fecharegistro;
   }

   @Column(name = "fechamodificacion", nullable = false, length = 20)
   public String getFechamodificacion()
   {
      return this.fechamodificacion;
   }

   public void setFechamodificacion(String fechamodificacion)
   {
      this.fechamodificacion = fechamodificacion;
   }

   @Column(name = "idcentro")
   public Integer getIdcentro()
   {
      return this.idcentro;
   }

   public void setIdcentro(Integer idcentro)
   {
      this.idcentro = idcentro;
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