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

import org.almacen.model.Egreso;

/**
 * Backing bean for Egreso entities.
 * <p/>
 * This class provides CRUD functionality for all Egreso entities. It focuses
 * purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt> for
 * state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD framework or
 * custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class EgresoBean implements Serializable
{

   private static final long serialVersionUID = 1L;

   /*
    * Support creating and retrieving Egreso entities
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

   private Egreso egreso;

   public Egreso getEgreso()
   {
      return this.egreso;
   }

   public void setEgreso(Egreso egreso)
   {
      this.egreso = egreso;
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
         this.egreso = this.example;
      }
      else
      {
         this.egreso = findById(getId());
      }
   }

   public Egreso findById(Integer id)
   {

      return this.entityManager.find(Egreso.class, id);
   }

   /*
    * Support updating and deleting Egreso entities
    */

   public String update()
   {
      this.conversation.end();

      try
      {
         if (this.id == null)
         {
            this.entityManager.persist(this.egreso);
            return "search?faces-redirect=true";
         }
         else
         {
            this.entityManager.merge(this.egreso);
            return "view?faces-redirect=true&id=" + this.egreso.getIdegreso();
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
         Egreso deletableEntity = findById(getId());

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
    * Support searching Egreso entities with pagination
    */

   private int page;
   private long count;
   private List<Egreso> pageItems;

   private Egreso example = new Egreso();

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

   public Egreso getExample()
   {
      return this.example;
   }

   public void setExample(Egreso example)
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
      Root<Egreso> root = countCriteria.from(Egreso.class);
      countCriteria = countCriteria.select(builder.count(root)).where(
            getSearchPredicates(root));
      this.count = this.entityManager.createQuery(countCriteria)
            .getSingleResult();

      // Populate this.pageItems

      CriteriaQuery<Egreso> criteria = builder.createQuery(Egreso.class);
      root = criteria.from(Egreso.class);
      TypedQuery<Egreso> query = this.entityManager.createQuery(criteria
            .select(root).where(getSearchPredicates(root)));
      query.setFirstResult(this.page * getPageSize()).setMaxResults(
            getPageSize());
      this.pageItems = query.getResultList();
   }

   private Predicate[] getSearchPredicates(Root<Egreso> root)
   {

      CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
      List<Predicate> predicatesList = new ArrayList<Predicate>();

      Integer cantidad = this.example.getCantidad();
      if (cantidad != null && cantidad.intValue() != 0)
      {
         predicatesList.add(builder.equal(root.get("cantidad"), cantidad));
      }
      String fechaingreso = this.example.getFechaingreso();
      if (fechaingreso != null && !"".equals(fechaingreso))
      {
         predicatesList.add(builder.like(builder.lower(root.<String> get("fechaingreso")), '%' + fechaingreso.toLowerCase() + '%'));
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
      String observacion = this.example.getObservacion();
      if (observacion != null && !"".equals(observacion))
      {
         predicatesList.add(builder.like(builder.lower(root.<String> get("observacion")), '%' + observacion.toLowerCase() + '%'));
      }

      return predicatesList.toArray(new Predicate[predicatesList.size()]);
   }

   public List<Egreso> getPageItems()
   {
      return this.pageItems;
   }

   public long getCount()
   {
      return this.count;
   }

   /*
    * Support listing and POSTing back Egreso entities (e.g. from inside an
    * HtmlSelectOneMenu)
    */

   public List<Egreso> getAll()
   {

      CriteriaQuery<Egreso> criteria = this.entityManager
            .getCriteriaBuilder().createQuery(Egreso.class);
      return this.entityManager.createQuery(
            criteria.select(criteria.from(Egreso.class))).getResultList();
   }

   @Resource
   private SessionContext sessionContext;

   public Converter getConverter()
   {

      final EgresoBean ejbProxy = this.sessionContext.getBusinessObject(EgresoBean.class);

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

            return String.valueOf(((Egreso) value).getIdegreso());
         }
      };
   }

   /*
    * Support adding children to bidirectional, one-to-many tables
    */

   private Egreso add = new Egreso();

   public Egreso getAdd()
   {
      return this.add;
   }

   public Egreso getAdded()
   {
      Egreso added = this.add;
      this.add = new Egreso();
      return added;
   }
}
