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

import org.almacen.model.Sexo;

/**
 * Backing bean for Sexo entities.
 * <p/>
 * This class provides CRUD functionality for all Sexo entities. It focuses
 * purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt> for
 * state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD framework or
 * custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class SexoBean implements Serializable
{

   private static final long serialVersionUID = 1L;

   /*
    * Support creating and retrieving Sexo entities
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

   private Sexo sexo;

   public Sexo getSexo()
   {
      return this.sexo;
   }

   public void setSexo(Sexo sexo)
   {
      this.sexo = sexo;
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
         this.sexo = this.example;
      }
      else
      {
         this.sexo = findById(getId());
      }
   }

   public Sexo findById(Integer id)
   {

      return this.entityManager.find(Sexo.class, id);
   }

   /*
    * Support updating and deleting Sexo entities
    */

   public String update()
   {
      this.conversation.end();

      try
      {
         if (this.id == null)
         {
            this.entityManager.persist(this.sexo);
            return "search?faces-redirect=true";
         }
         else
         {
            this.entityManager.merge(this.sexo);
            return "view?faces-redirect=true&id=" + this.sexo.getIdsexo();
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
         Sexo deletableEntity = findById(getId());

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
    * Support searching Sexo entities with pagination
    */

   private int page;
   private long count;
   private List<Sexo> pageItems;

   private Sexo example = new Sexo();

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

   public Sexo getExample()
   {
      return this.example;
   }

   public void setExample(Sexo example)
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
      Root<Sexo> root = countCriteria.from(Sexo.class);
      countCriteria = countCriteria.select(builder.count(root)).where(
            getSearchPredicates(root));
      this.count = this.entityManager.createQuery(countCriteria)
            .getSingleResult();

      // Populate this.pageItems

      CriteriaQuery<Sexo> criteria = builder.createQuery(Sexo.class);
      root = criteria.from(Sexo.class);
      TypedQuery<Sexo> query = this.entityManager.createQuery(criteria
            .select(root).where(getSearchPredicates(root)));
      query.setFirstResult(this.page * getPageSize()).setMaxResults(
            getPageSize());
      this.pageItems = query.getResultList();
   }

   private Predicate[] getSearchPredicates(Root<Sexo> root)
   {

      CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
      List<Predicate> predicatesList = new ArrayList<Predicate>();

      String sexo = this.example.getSexo();
      if (sexo != null && !"".equals(sexo))
      {
         predicatesList.add(builder.like(builder.lower(root.<String> get("sexo")), '%' + sexo.toLowerCase() + '%'));
      }

      return predicatesList.toArray(new Predicate[predicatesList.size()]);
   }

   public List<Sexo> getPageItems()
   {
      return this.pageItems;
   }

   public long getCount()
   {
      return this.count;
   }

   /*
    * Support listing and POSTing back Sexo entities (e.g. from inside an
    * HtmlSelectOneMenu)
    */

   public List<Sexo> getAll()
   {

      CriteriaQuery<Sexo> criteria = this.entityManager
            .getCriteriaBuilder().createQuery(Sexo.class);
      return this.entityManager.createQuery(
            criteria.select(criteria.from(Sexo.class))).getResultList();
   }

   @Resource
   private SessionContext sessionContext;

   public Converter getConverter()
   {

      final SexoBean ejbProxy = this.sessionContext.getBusinessObject(SexoBean.class);

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

            return String.valueOf(((Sexo) value).getIdsexo());
         }
      };
   }

   /*
    * Support adding children to bidirectional, one-to-many tables
    */

   private Sexo add = new Sexo();

   public Sexo getAdd()
   {
      return this.add;
   }

   public Sexo getAdded()
   {
      Sexo added = this.add;
      this.add = new Sexo();
      return added;
   }
}
