package org.almacen.model;

// Generated 08-may-2015 17:42:17 by Hibernate Tools 4.3.1

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Material generated by hbm2java
 */
@Entity
@Table(name = "material"
      , catalog = "bdalmacen")
public class Material implements java.io.Serializable
{

   private Integer idmaterial;
   private String codigomaterial;
   private String descripcion;
   private String fecharegistro;
   private String fechamodificacion;
   private Integer idunidadmedida;
   private Integer idsubcategoria;
   private Integer idestado;
   private Integer idusuario;

   public Material()
   {
   }

   public Material(String codigomaterial, String descripcion)
   {
      this.codigomaterial = codigomaterial;
      this.descripcion = descripcion;
   }

   public Material(String codigomaterial, String descripcion, String fecharegistro, String fechamodificacion, Integer idunidadmedida, Integer idsubcategoria, Integer idestado, Integer idusuario)
   {
      this.codigomaterial = codigomaterial;
      this.descripcion = descripcion;
      this.fecharegistro = fecharegistro;
      this.fechamodificacion = fechamodificacion;
      this.idunidadmedida = idunidadmedida;
      this.idsubcategoria = idsubcategoria;
      this.idestado = idestado;
      this.idusuario = idusuario;
   }

   @Id
   @GeneratedValue(strategy = IDENTITY)
   @Column(name = "idmaterial", unique = true, nullable = false)
   public Integer getIdmaterial()
   {
      return this.idmaterial;
   }

   public void setIdmaterial(Integer idmaterial)
   {
      this.idmaterial = idmaterial;
   }

   @Column(name = "codigomaterial", nullable = false, length = 50)
   public String getCodigomaterial()
   {
      return this.codigomaterial;
   }

   public void setCodigomaterial(String codigomaterial)
   {
      this.codigomaterial = codigomaterial;
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

   @Column(name = "idunidadmedida")
   public Integer getIdunidadmedida()
   {
      return this.idunidadmedida;
   }

   public void setIdunidadmedida(Integer idunidadmedida)
   {
      this.idunidadmedida = idunidadmedida;
   }

   @Column(name = "idsubcategoria")
   public Integer getIdsubcategoria()
   {
      return this.idsubcategoria;
   }

   public void setIdsubcategoria(Integer idsubcategoria)
   {
      this.idsubcategoria = idsubcategoria;
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
