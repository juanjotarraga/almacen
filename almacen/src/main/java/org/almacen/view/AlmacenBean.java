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

import org.almacen.model.Almacen;

/**
 * Backing bean for Almacen entities.
 * <p/>
 * This class provides CRUD functionality for all Almacen entities. It focuses
 * purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt> for
 * state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD framework or
 * custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class AlmacenBean implements Serializable
{

   private static final long serialVersionUID = 1L;

   /*
    * Support creating and retrieving Almacen entities
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

   private Almacen almacen;

   public Almacen getAlmacen()
   {
      return this.almacen;
   }

   public void setAlmacen(Almacen almacen)
   {
      this.almacen = almacen;
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
         this.almacen = this.example;
      }
      else
      {
         this.almacen = findById(getId());
      }
   }

   public Almacen findById(Integer id)
   {

      return this.entityManager.find(Almacen.class, id);
   }

   /*
    * Support updating and deleting Almacen entities
    */

   public String update()
   {
      this.conversation.end();

      try
      {
         if (this.id == null)
         {
            this.entityManager.persist(this.almacen);
            return "search?faces-redirect=true";
         }
         else
         {
            this.entityManager.merge(this.almacen);
            return "view?faces-redirect=true&id=" + this.almacen.getIdalmacen();
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
         Almacen deletableEntity = findById(getId());

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
    * Support searching Almacen entities with pagination
    */

   private int page;
   private long count;
   private List<Almacen> pageItems;

   private Almacen example = new Almacen();

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

   public Almacen getExample()
   {
      return this.example;
   }

   public void setExample(Almacen example)
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
      Root<Almacen> root = countCriteria.from(Almacen.class);
      countCriteria = countCriteria.select(builder.count(root)).where(
            getSearchPredicates(root));
      this.count = this.entityManager.createQuery(countCriteria)
            .getSingleResult();

      // Populate this.pageItems

      CriteriaQuery<Almacen> criteria = builder.createQuery(Almacen.class);
      root = criteria.from(Almacen.class);
      TypedQuery<Almacen> query = this.entityManager.createQuery(criteria
            .select(root).where(getSearchPredicates(root)));
      query.setFirstResult(this.page * getPageSize()).setMaxResults(
            getPageSize());
      this.pageItems = query.getResultList();
   }

   private Predicate[] getSearchPredicates(Root<Almacen> root)
   {

      CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
      List<Predicate> predicatesList = new ArrayList<Predicate>();

      String almacen = this.example.getAlmacen();
      if (almacen != null && !"".equals(almacen))
      {
         predicatesList.add(builder.like(builder.lower(root.<String> get("almacen")), '%' + almacen.toLowerCase() + '%'));
      }
      String codigoalmacen = this.example.getCodigoalmacen();
      if (codigoalmacen != null && !"".equals(codigoalmacen))
      {
         predicatesList.add(builder.like(builder.lower(root.<String> get("codigoalmacen")), '%' + codigoalmacen.toLowerCase() + '%'));
      }
      String representante = this.example.getRepresentante();
      if (representante != null && !"".equals(representante))
      {
         predicatesList.add(builder.like(builder.lower(root.<String> get("representante")), '%' + representante.toLowerCase() + '%'));
      }
      String observacion = this.example.getObservacion();
      if (observacion != null && !"".equals(observacion))
      {
         predicatesList.add(builder.like(builder.lower(root.<String> get("observacion")), '%' + observacion.toLowerCase() + '%'));
      }
      String fecharegistro = this.example.getFecharegistro();
      if (fecharegistro != null && !"".equals(fecharegistro))
      {
         predicatesList.add(builder.like(builder.lower(root.<String> get("fecharegistro")), '%' + fecharegistro.toLowerCase() + '%'));
      }

      return predicatesList.toArray(new Predicate[predicatesList.size()]);
   }

   public List<Almacen> getPageItems()
   {
      return this.pageItems;
   }

   public long getCount()
   {
      return this.count;
   }

   /*
    * Support listing and POSTing back Almacen entities (e.g. from inside an
    * HtmlSelectOneMenu)
    */

   public List<Almacen> getAll()
   {

      CriteriaQuery<Almacen> criteria = this.entityManager
            .getCriteriaBuilder().createQuery(Almacen.class);
      return this.entityManager.createQuery(
            criteria.select(criteria.from(Almacen.class))).getResultList();
   }

   @Resource
   private SessionContext sessionContext;

   public Converter getConverter()
   {

      final AlmacenBean ejbProxy = this.sessionContext.getBusinessObject(AlmacenBean.class);

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

            return String.valueOf(((Almacen) value).getIdalmacen());
         }
      };
   }

   /*
    * Support adding children to bidirectional, one-to-many tables
    */

   private Almacen add = new Almacen();

   public Almacen getAdd()
   {
      return this.add;
   }

   public Almacen getAdded()
   {
      Almacen added = this.add;
      this.add = new Almacen();
      return added;
   }
}
