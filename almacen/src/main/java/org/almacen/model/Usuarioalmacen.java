package org.almacen.model;

// Generated 08-may-2015 17:42:17 by Hibernate Tools 4.3.1

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Usuarioalmacen generated by hbm2java
 */
@Entity
@Table(name = "usuarioalmacen"
      , catalog = "bdalmacen")
public class Usuarioalmacen implements java.io.Serializable
{

   private Integer idusuarioalmacen;
   private String fechaasignacion;
   private String fecharegistro;
   private String fechamodificacion;
   private Integer idalmacen;
   private Integer idusuario;
   private Integer idestado;

   public Usuarioalmacen()
   {
   }

   public Usuarioalmacen(String fechaasignacion, String fecharegistro, String fechamodificacion, Integer idalmacen, Integer idusuario, Integer idestado)
   {
      this.fechaasignacion = fechaasignacion;
      this.fecharegistro = fecharegistro;
      this.fechamodificacion = fechamodificacion;
      this.idalmacen = idalmacen;
      this.idusuario = idusuario;
      this.idestado = idestado;
   }

   @Id
   @GeneratedValue(strategy = IDENTITY)
   @Column(name = "idusuarioalmacen", unique = true, nullable = false)
   public Integer getIdusuarioalmacen()
   {
      return this.idusuarioalmacen;
   }

   public void setIdusuarioalmacen(Integer idusuarioalmacen)
   {
      this.idusuarioalmacen = idusuarioalmacen;
   }

   @Column(name = "fechaasignacion", length = 20)
   public String getFechaasignacion()
   {
      return this.fechaasignacion;
   }

   public void setFechaasignacion(String fechaasignacion)
   {
      this.fechaasignacion = fechaasignacion;
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

   @Column(name = "idalmacen")
   public Integer getIdalmacen()
   {
      return this.idalmacen;
   }

   public void setIdalmacen(Integer idalmacen)
   {
      this.idalmacen = idalmacen;
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