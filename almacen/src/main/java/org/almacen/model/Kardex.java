package org.almacen.model;

// Generated 08-may-2015 17:42:17 by Hibernate Tools 4.3.1

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Kardex generated by hbm2java
 */
@Entity
@Table(name = "kardex"
      , catalog = "bdalmacen")
public class Kardex implements java.io.Serializable
{

   private Integer idkardex;
   private String descripcion;
   private String nroestante;
   private String fecharegistro;
   private String fechamodificacion;
   private Integer idalmacenmaterial;
   private Integer idestado;
   private Integer idusuario;

   public Kardex()
   {
   }

   public Kardex(String descripcion, String nroestante, String fecharegistro, String fechamodificacion, Integer idalmacenmaterial, Integer idestado, Integer idusuario)
   {
      this.descripcion = descripcion;
      this.nroestante = nroestante;
      this.fecharegistro = fecharegistro;
      this.fechamodificacion = fechamodificacion;
      this.idalmacenmaterial = idalmacenmaterial;
      this.idestado = idestado;
      this.idusuario = idusuario;
   }

   @Id
   @GeneratedValue(strategy = IDENTITY)
   @Column(name = "idkardex", unique = true, nullable = false)
   public Integer getIdkardex()
   {
      return this.idkardex;
   }

   public void setIdkardex(Integer idkardex)
   {
      this.idkardex = idkardex;
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

   @Column(name = "nroestante", length = 20)
   public String getNroestante()
   {
      return this.nroestante;
   }

   public void setNroestante(String nroestante)
   {
      this.nroestante = nroestante;
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

   @Column(name = "idalmacenmaterial")
   public Integer getIdalmacenmaterial()
   {
      return this.idalmacenmaterial;
   }

   public void setIdalmacenmaterial(Integer idalmacenmaterial)
   {
      this.idalmacenmaterial = idalmacenmaterial;
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

   @Column(name = "idusuario")
   public Integer getIdusuario()
   {
      return this.idusuario;
   }

   public void setIdusuario(Integer idusuario)
   {
      this.idusuario = idusuario;
   }

}