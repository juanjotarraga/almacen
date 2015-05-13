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

import org.almacen.model.Estado;

/**
 * Backing bean for Estado entities.
 * <p/>
 * This class provides CRUD functionality for all Estado entities. It focuses
 * purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt> for
 * state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD framework or
 * custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class EstadoBean implements Serializable
{

   private static final long serialVersionUID = 1L;

   /*
    * Support creating and retrieving Estado entities
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

   private Estado estado;

   public Estado getEstado()
   {
      return this.estado;
   }

   public void setEstado(Estado estado)
   {
      this.estado = estado;
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
         this.estado = this.example;
      }
      else
      {
         this.estado = findById(getId());
      }
   }

   public Estado findById(Integer id)
   {

      return this.entityManager.find(Estado.class, id);
   }

   /*
    * Support updating and deleting Estado entities
    */

   public String update()
   {
      this.conversation.end();

      try
      {
         if (this.id == null)
         {
            this.entityManager.persist(this.estado);
            return "search?faces-redirect=true";
         }
         else
         {
            this.entityManager.merge(this.estado);
            return "view?faces-redirect=true&id=" + this.estado.getIdestado();
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
         Estado deletableEntity = findById(getId());

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
    * Support searching Estado entities with pagination
    */

   private int page;
   private long count;
   private List<Estado> pageItems;

   private Estado example = new Estado();

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

   public Estado getExample()
   {
      return this.example;
   }

   public void setExample(Estado example)
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
      Root<Estado> root = countCriteria.from(Estado.class);
      countCriteria = countCriteria.select(builder.count(root)).where(
            getSearchPredicates(root));
      this.count = this.entityManager.createQuery(countCriteria)
            .getSingleResult();

      // Populate this.pageItems

      CriteriaQuery<Estado> criteria = builder.createQuery(Estado.class);
      root = criteria.from(Estado.class);
      TypedQuery<Estado> query = this.entityManager.createQuery(criteria
            .select(root).where(getSearchPredicates(root)));
      query.setFirstResult(this.page * getPageSize()).setMaxResults(
            getPageSize());
      this.pageItems = query.getResultList();
   }

   private Predicate[] getSearchPredicates(Root<Estado> root)
   {

      CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
      List<Predicate> predicatesList = new ArrayList<Predicate>();

      String estado = this.example.getEstado();
      if (estado != null && !"".equals(estado))
      {
         predicatesList.add(builder.like(builder.lower(root.<String> get("estado")), '%' + estado.toLowerCase() + '%'));
      }

      return predicatesList.toArray(new Predicate[predicatesList.size()]);
   }

   public List<Estado> getPageItems()
   {
      return this.pageItems;
   }

   public long getCount()
   {
      return this.count;
   }

   /*
    * Support listing and POSTing back Estado entities (e.g. from inside an
    * HtmlSelectOneMenu)
    */

   public List<Estado> getAll()
   {

      CriteriaQuery<Estado> criteria = this.entityManager
            .getCriteriaBuilder().createQuery(Estado.class);
      return this.entityManager.createQuery(
            criteria.select(criteria.from(Estado.class))).getResultList();
   }

   @Resource
   private SessionContext sessionContext;

   public Converter getConverter()
   {

      final EstadoBean ejbProxy = this.sessionContext.getBusinessObject(EstadoBean.class);

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

            return String.valueOf(((Estado) value).getIdestado());
         }
      };
   }

   /*
    * Support adding children to bidirectional, one-to-many tables
    */

   private Estado add = new Estado();

   public Estado getAdd()
   {
      return this.add;
   }

   public Estado getAdded()
   {
      Estado added = this.add;
      this.add = new Estado();
      return added;
   }
}
