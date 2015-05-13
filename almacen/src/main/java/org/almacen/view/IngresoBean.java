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

import org.almacen.model.Ingreso;

/**
 * Backing bean for Ingreso entities.
 * <p/>
 * This class provides CRUD functionality for all Ingreso entities. It focuses
 * purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt> for
 * state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD framework or
 * custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class IngresoBean implements Serializable
{

   private static final long serialVersionUID = 1L;

   /*
    * Support creating and retrieving Ingreso entities
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

   private Ingreso ingreso;

   public Ingreso getIngreso()
   {
      return this.ingreso;
   }

   public void setIngreso(Ingreso ingreso)
   {
      this.ingreso = ingreso;
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
         this.ingreso = this.example;
      }
      else
      {
         this.ingreso = findById(getId());
      }
   }

   public Ingreso findById(Integer id)
   {

      return this.entityManager.find(Ingreso.class, id);
   }

   /*
    * Support updating and deleting Ingreso entities
    */

   public String update()
   {
      this.conversation.end();

      try
      {
         if (this.id == null)
         {
            this.entityManager.persist(this.ingreso);
            return "search?faces-redirect=true";
         }
         else
         {
            this.entityManager.merge(this.ingreso);
            return "view?faces-redirect=true&id=" + this.ingreso.getIdingreso();
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
         Ingreso deletableEntity = findById(getId());

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
    * Support searching Ingreso entities with pagination
    */

   private int page;
   private long count;
   private List<Ingreso> pageItems;

   private Ingreso example = new Ingreso();

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

   public Ingreso getExample()
   {
      return this.example;
   }

   public void setExample(Ingreso example)
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
      Root<Ingreso> root = countCriteria.from(Ingreso.class);
      countCriteria = countCriteria.select(builder.count(root)).where(
            getSearchPredicates(root));
      this.count = this.entityManager.createQuery(countCriteria)
            .getSingleResult();

      // Populate this.pageItems

      CriteriaQuery<Ingreso> criteria = builder.createQuery(Ingreso.class);
      root = criteria.from(Ingreso.class);
      TypedQuery<Ingreso> query = this.entityManager.createQuery(criteria
            .select(root).where(getSearchPredicates(root)));
      query.setFirstResult(this.page * getPageSize()).setMaxResults(
            getPageSize());
      this.pageItems = query.getResultList();
   }

   private Predicate[] getSearchPredicates(Root<Ingreso> root)
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
      String nrofactura = this.example.getNrofactura();
      if (nrofactura != null && !"".equals(nrofactura))
      {
         predicatesList.add(builder.like(builder.lower(root.<String> get("nrofactura")), '%' + nrofactura.toLowerCase() + '%'));
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

      return predicatesList.toArray(new Predicate[predicatesList.size()]);
   }

   public List<Ingreso> getPageItems()
   {
      return this.pageItems;
   }

   public long getCount()
   {
      return this.count;
   }

   /*
    * Support listing and POSTing back Ingreso entities (e.g. from inside an
    * HtmlSelectOneMenu)
    */

   public List<Ingreso> getAll()
   {

      CriteriaQuery<Ingreso> criteria = this.entityManager
            .getCriteriaBuilder().createQuery(Ingreso.class);
      return this.entityManager.createQuery(
            criteria.select(criteria.from(Ingreso.class))).getResultList();
   }

   @Resource
   private SessionContext sessionContext;

   public Converter getConverter()
   {

      final IngresoBean ejbProxy = this.sessionContext.getBusinessObject(IngresoBean.class);

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

            return String.valueOf(((Ingreso) value).getIdingreso());
         }
      };
   }

   /*
    * Support adding children to bidirectional, one-to-many tables
    */

   private Ingreso add = new Ingreso();

   public Ingreso getAdd()
   {
      return this.add;
   }

   public Ingreso getAdded()
   {
      Ingreso added = this.add;
      this.add = new Ingreso();
      return added;
   }
}
