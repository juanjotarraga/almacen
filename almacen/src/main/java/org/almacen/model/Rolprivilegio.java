package org.almacen.model;

// Generated 08-may-2015 17:42:17 by Hibernate Tools 4.3.1

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Rolprivilegio generated by hbm2java
 */
@Entity
@Table(name = "rolprivilegio"
      , catalog = "bdalmacen")
public class Rolprivilegio implements java.io.Serializable
{

   private Integer idrolprivilegio;
   private String rolprivilegio;
   private String descripcion;
   private String ruta;
   private String fecharegistro;
   private String fechamodificacion;
   private Integer idenlace;
   private Integer idrol;
   private Integer idestado;

   public Rolprivilegio()
   {
   }

   public Rolprivilegio(String rolprivilegio, String descripcion, String ruta)
   {
      this.rolprivilegio = rolprivilegio;
      this.descripcion = descripcion;
      this.ruta = ruta;
   }

   public Rolprivilegio(String rolprivilegio, String descripcion, String ruta, String fecharegistro, String fechamodificacion, Integer idenlace, Integer idrol, Integer idestado)
   {
      this.rolprivilegio = rolprivilegio;
      this.descripcion = descripcion;
      this.ruta = ruta;
      this.fecharegistro = fecharegistro;
      this.fechamodificacion = fechamodificacion;
      this.idenlace = idenlace;
      this.idrol = idrol;
      this.idestado = idestado;
   }

   @Id
   @GeneratedValue(strategy = IDENTITY)
   @Column(name = "idrolprivilegio", unique = true, nullable = false)
   public Integer getIdrolprivilegio()
   {
      return this.idrolprivilegio;
   }

   public void setIdrolprivilegio(Integer idrolprivilegio)
   {
      this.idrolprivilegio = idrolprivilegio;
   }

   @Column(name = "rolprivilegio", nullable = false, length = 50)
   public String getRolprivilegio()
   {
      return this.rolprivilegio;
   }

   public void setRolprivilegio(String rolprivilegio)
   {
      this.rolprivilegio = rolprivilegio;
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

   @Column(name = "ruta", nullable = false, length = 100)
   public String getRuta()
   {
      return this.ruta;
   }

   public void setRuta(String ruta)
   {
      this.ruta = ruta;
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

   @Column(name = "idenlace")
   public Integer getIdenlace()
   {
      return this.idenlace;
   }

   public void setIdenlace(Integer idenlace)
   {
      this.idenlace = idenlace;
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
