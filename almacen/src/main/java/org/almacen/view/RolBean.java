package org.almacen.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateful;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.almacen.model.Rol;

/**
 * Backing bean for Rol entities.
 * <p/>
 * This class provides CRUD functionality for all Rol entities. It focuses
 * purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt> for
 * state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD framework or
 * custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class RolBean implements Serializable
{

   private static final long serialVersionUID = 1L;

   /*
    * Support creating and retrieving Rol entities
    */

   private Integer id;

   public Integer getId()
   {
      return this.id;
   }

   public void setId(Integer id)
   {
      this.id = id;
   }

   private Rol rol;

   public Rol getRol()
   {
      return this.rol;
   }

   public void setRol(Rol rol)
   {
      this.rol = rol;
   }

   @Inject
   private Conversation conversation;

   @PersistenceContext(unitName = "almacen-persistence-unit", type = PersistenceContextType.EXTENDED)
   private EntityManager entityManager;

   public String create()
   {

      this.conversation.begin();
      this.conversation.setTimeout(1800000L);
      return "create?faces-redirect=true";
   }

   public void retrieve()
   {

      if (FacesContext.getCurrentInstance().isPostback())
      {
         return;
      }

      if (this.conversation.isTransient())
      {
         this.conversation.begin();
         this.conversation.setTimeout(1800000L);
      }

      if (this.id == null)
      {
         this.rol = this.example;
      }
      else
      {
         this.rol = findById(getId());
      }
   }

   public Rol findById(Integer id)
   {

      return this.entityManager.find(Rol.class, id);
   }

   /*
    * Support updating and deleting Rol entities
    */

   public String update()
   {
      this.conversation.end();

      try
      {
         if (this.id == null)
         {
            this.entityManager.persist(this.rol);
            return "search?faces-redirect=true";
         }
         else
         {
            this.entityManager.merge(this.rol);
            return "view?faces-redirect=true&id=" + this.rol.getIdrol();
         }
      }
      catch (Exception e)
      {
         FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
         return null;
      }
   }

   public String delete()
   {
      this.conversation.end();

      try
      {
         Rol deletableEntity = findById(getId());

         this.entityManager.remove(deletableEntity);
         this.entityManager.flush();
         return "search?faces-redirect=true";
      }
      catch (Exception e)
      {
         FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
         return null;
      }
   }

   /*
    * Support searching Rol entities with pagination
    */

   private int page;
   private long count;
   private List<Rol> pageItems;

   private Rol example = new Rol();

   public int getPage()
   {
      return this.page;
   }

   public void setPage(int page)
   {
      this.page = page;
   }

   public int getPageSize()
   {
      return 10;
   }

   public Rol getExample()
   {
      return this.example;
   }

   public void setExample(Rol example)
   {
      this.example = example;
   }

   public String search()
   {
      this.page = 0;
      return null;
   }

   public void paginate()
   {

      CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();

      // Populate this.count

      CriteriaQuery<Long> countCriteria = builder.createQuery(Long.class);
      Root<Rol> root = countCriteria.from(Rol.class);
      countCriteria = countCriteria.select(builder.count(root)).where(
            getSearchPredicates(root));
      this.count = this.entityManager.createQuery(countCriteria)
            .getSingleResult();

      // Populate this.pageItems

      CriteriaQuery<Rol> criteria = builder.createQuery(Rol.class);
      root = criteria.from(Rol.class);
      TypedQuery<Rol> query = this.entityManager.createQuery(criteria
            .select(root).where(getSearchPredicates(root)));
      query.setFirstResult(this.page * getPageSize()).setMaxResults(
            getPageSize());
      this.pageItems = query.getResultList();
   }

   private Predicate[] getSearchPredicates(Root<Rol> root)
   {

      CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
      List<Predicate> predicatesList = new ArrayList<Predicate>();

      String rol = this.example.getRol();
      if (rol != null && !"".equals(rol))
      {
         predicatesList.add(builder.like(builder.lower(root.<String> get("rol")), '%' + rol.toLowerCase() + '%'));
      }
      String descripcion = this.example.getDescripcion();
      if (descripcion != null && !"".equals(descripcion))
      {
         predicatesList.add(builder.like(builder.lower(root.<String> get("descripcion")), '%' + descripcion.toLowerCase() + '%'));
      }
      String fecharegistro = this.example.getFecharegistro();
      if (fecharegistro != null && !"".equals(fecharegistro))
      {
         predicatesList.add(builder.like(builder.lower(root.<String> get("fecharegistro")), '%' + fecharegistro.toLowerCase() + '%'));
      }
      String fechamodificacion = this.example.getFechamodificacion();
      if (fechamodificacion != null && !"".equals(fechamodificacion))
      {
         predicatesList.add(builder.like(builder.lower(root.<String> get("fechamodificacion")), '%' + fechamodificacion.toLowerCase() + '%'));
      }
      Integer idestado = this.example.getIdestado();
      if (idestado != null && idestado.intValue() != 0)
      {
         predicatesList.add(builder.equal(root.get("idestado"), idestado));
      }

      return predicatesList.toArray(new Predicate[predicatesList.size()]);
   }

   public List<Rol> getPageItems()
   {
      return this.pageItems;
   }

   public long getCount()
   {
      return this.count;
   }

   /*
    * Support listing and POSTing back Rol entities (e.g. from inside an
    * HtmlSelectOneMenu)
    */

   public List<Rol> getAll()
   {

      CriteriaQuery<Rol> criteria = this.entityManager
            .getCriteriaBuilder().createQuery(Rol.class);
      return this.entityManager.createQuery(
            criteria.select(criteria.from(Rol.class))).getResultList();
   }

   @Resource
   private SessionContext sessionContext;

   public Converter getConverter()
   {

      final RolBean ejbProxy = this.sessionContext.getBusinessObject(RolBean.class);

      return new Converter()
      {

         @Override
         public Object getAsObject(FacesContext context,
               UIComponent component, String value)
         {

            return ejbProxy.findById(Integer.valueOf(value));
         }

         @Override
         public String getAsString(FacesContext context,
               UIComponent component, Object value)
         {

            if (value == null)
            {
               return "";
            }

            return String.valueOf(((Rol) value).getIdrol());
         }
      };
   }

   /*
    * Support adding children to bidirectional, one-to-many tables
    */

   private Rol add = new Rol();

   public Rol getAdd()
   {
      return this.add;
   }

   public Rol getAdded()
   {
      Rol added = this.add;
      this.add = new Rol();
      return added;
   }
}
