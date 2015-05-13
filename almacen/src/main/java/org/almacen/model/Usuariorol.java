package org.almacen.model;

// Generated 08-may-2015 17:42:17 by Hibernate Tools 4.3.1

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Usuariorol generated by hbm2java
 */
@Entity
@Table(name = "usuariorol"
      , catalog = "bdalmacen")
public class Usuariorol implements java.io.Serializable
{

   private Integer idusuariorol;
   private String fechaexpiracion;
   private String fecharegistro;
   private String fechamodificacion;
   private Integer idrol;
   private Integer idusuario;
   private Integer idestado;

   public Usuariorol()
   {
   }

   public Usuariorol(String fechaexpiracion, String fecharegistro, String fechamodificacion, Integer idrol, Integer idusuario, Integer idestado)
   {
      this.fechaexpiracion = fechaexpiracion;
      this.fecharegistro = fecharegistro;
      this.fechamodificacion = fechamodificacion;
      this.idrol = idrol;
      this.idusuario = idusuario;
      this.idestado = idestado;
   }

   @Id
   @GeneratedValue(strategy = IDENTITY)
   @Column(name = "idusuariorol", unique = true, nullable = false)
   public Integer getIdusuariorol()
   {
      return this.idusuariorol;
   }

   public void setIdusuariorol(Integer idusuariorol)
   {
      this.idusuariorol = idusuariorol;
   }

   @Column(name = "fechaexpiracion", length = 20)
   public String getFechaexpiracion()
   {
      return this.fechaexpiracion;
   }

   public void setFechaexpiracion(String fechaexpiracion)
   {
      this.fechaexpiracion = fechaexpiracion;
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

   @Column(name = "idrol")
   public Integer getIdrol()
   {
      return this.idrol;
   }

   public void setIdrol(Integer idrol)
   {
      this.idrol = idrol;
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
