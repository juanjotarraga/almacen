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

import org.almacen.model.Periodoalmacen;

/**
 * Backing bean for Periodoalmacen entities.
 * <p/>
 * This class provides CRUD functionality for all Periodoalmacen entities. It focuses
 * purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt> for
 * state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD framework or
 * custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class PeriodoalmacenBean implements Serializable
{

   private static final long serialVersionUID = 1L;

   /*
    * Support creating and retrieving Periodoalmacen entities
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

   private Periodoalmacen periodoalmacen;

   public Periodoalmacen getPeriodoalmacen()
   {
      return this.periodoalmacen;
   }

   public void setPeriodoalmacen(Periodoalmacen periodoalmacen)
   {
      this.periodoalmacen = periodoalmacen;
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
         this.periodoalmacen = this.example;
      }
      else
      {
         this.periodoalmacen = findById(getId());
      }
   }

   public Periodoalmacen findById(Integer id)
   {

      return this.entityManager.find(Periodoalmacen.class, id);
   }

   /*
    * Support updating and deleting Periodoalmacen entities
    */

   public String update()
   {
      this.conversation.end();

      try
      {
         if (this.id == null)
         {
            this.entityManager.persist(this.periodoalmacen);
            return "search?faces-redirect=true";
         }
         else
         {
            this.entityManager.merge(this.periodoalmacen);
            return "view?faces-redirect=true&id=" + this.periodoalmacen.getIdperiodoalmacen();
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
         Periodoalmacen deletableEntity = findById(getId());

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
    * Support searching Periodoalmacen entities with pagination
    */

   private int page;
   private long count;
   private List<Periodoalmacen> pageItems;

   private Periodoalmacen example = new Periodoalmacen();

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

   public Periodoalmacen getExample()
   {
      return this.example;
   }

   public void setExample(Periodoalmacen example)
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
      Root<Periodoalmacen> root = countCriteria.from(Periodoalmacen.class);
      countCriteria = countCriteria.select(builder.count(root)).where(
            getSearchPredicates(root));
      this.count = this.entityManager.createQuery(countCriteria)
            .getSingleResult();

      // Populate this.pageItems

      CriteriaQuery<Periodoalmacen> criteria = builder.createQuery(Periodoalmacen.class);
      root = criteria.from(Periodoalmacen.class);
      TypedQuery<Periodoalmacen> query = this.entityManager.createQuery(criteria
            .select(root).where(getSearchPredicates(root)));
      query.setFirstResult(this.page * getPageSize()).setMaxResults(
            getPageSize());
      this.pageItems = query.getResultList();
   }

   private Predicate[] getSearchPredicates(Root<Periodoalmacen> root)
   {

      CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
      List<Predicate> predicatesList = new ArrayList<Predicate>();

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
      Integer idalmacen = this.example.getIdalmacen();
      if (idalmacen != null && idalmacen.intValue() != 0)
      {
         predicatesList.add(builder.equal(root.get("idalmacen"), idalmacen));
      }
      Integer idestado = this.example.getIdestado();
      if (idestado != null && idestado.intValue() != 0)
      {
         predicatesList.add(builder.equal(root.get("idestado"), idestado));
      }

      return predicatesList.toArray(new Predicate[predicatesList.size()]);
   }

   public List<Periodoalmacen> getPageItems()
   {
      return this.pageItems;
   }

   public long getCount()
   {
      return this.count;
   }

   /*
    * Support listing and POSTing back Periodoalmacen entities (e.g. from inside an
    * HtmlSelectOneMenu)
    */

   public List<Periodoalmacen> getAll()
   {

      CriteriaQuery<Periodoalmacen> criteria = this.entityManager
            .getCriteriaBuilder().createQuery(Periodoalmacen.class);
      return this.entityManager.createQuery(
            criteria.select(criteria.from(Periodoalmacen.class))).getResultList();
   }

   @Resource
   private SessionContext sessionContext;

   public Converter getConverter()
   {

      final PeriodoalmacenBean ejbProxy = this.sessionContext.getBusinessObject(PeriodoalmacenBean.class);

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

            return String.valueOf(((Periodoalmacen) value).getIdperiodoalmacen());
         }
      };
   }

   /*
    * Support adding children to bidirectional, one-to-many tables
    */

   private Periodoalmacen add = new Periodoalmacen();

   public Periodoalmacen getAdd()
   {
      return this.add;
   }

   public Periodoalmacen getAdded()
   {
      Periodoalmacen added = this.add;
      this.add = new Periodoalmacen();
      return added;
   }
}
