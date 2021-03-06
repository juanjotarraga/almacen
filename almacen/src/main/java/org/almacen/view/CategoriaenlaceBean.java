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

import org.almacen.model.Categoriaenlace;

/**
 * Backing bean for Categoriaenlace entities.
 * <p/>
 * This class provides CRUD functionality for all Categoriaenlace entities. It focuses
 * purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt> for
 * state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD framework or
 * custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class CategoriaenlaceBean implements Serializable
{

   private static final long serialVersionUID = 1L;

   /*
    * Support creating and retrieving Categoriaenlace entities
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

   private Categoriaenlace categoriaenlace;

   public Categoriaenlace getCategoriaenlace()
   {
      return this.categoriaenlace;
   }

   public void setCategoriaenlace(Categoriaenlace categoriaenlace)
   {
      this.categoriaenlace = categoriaenlace;
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
         this.categoriaenlace = this.example;
      }
      else
      {
         this.categoriaenlace = findById(getId());
      }
   }

   public Categoriaenlace findById(Integer id)
   {

      return this.entityManager.find(Categoriaenlace.class, id);
   }

   /*
    * Support updating and deleting Categoriaenlace entities
    */

   public String update()
   {
      this.conversation.end();

      try
      {
         if (this.id == null)
         {
            this.entityManager.persist(this.categoriaenlace);
            return "search?faces-redirect=true";
         }
         else
         {
            this.entityManager.merge(this.categoriaenlace);
            return "view?faces-redirect=true&id=" + this.categoriaenlace.getIdcategoriaenlace();
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
         Categoriaenlace deletableEntity = findById(getId());

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
    * Support searching Categoriaenlace entities with pagination
    */

   private int page;
   private long count;
   private List<Categoriaenlace> pageItems;

   private Categoriaenlace example = new Categoriaenlace();

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

   public Categoriaenlace getExample()
   {
      return this.example;
   }

   public void setExample(Categoriaenlace example)
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
      Root<Categoriaenlace> root = countCriteria.from(Categoriaenlace.class);
      countCriteria = countCriteria.select(builder.count(root)).where(
            getSearchPredicates(root));
      this.count = this.entityManager.createQuery(countCriteria)
            .getSingleResult();

      // Populate this.pageItems

      CriteriaQuery<Categoriaenlace> criteria = builder.createQuery(Categoriaenlace.class);
      root = criteria.from(Categoriaenlace.class);
      TypedQuery<Categoriaenlace> query = this.entityManager.createQuery(criteria
            .select(root).where(getSearchPredicates(root)));
      query.setFirstResult(this.page * getPageSize()).setMaxResults(
            getPageSize());
      this.pageItems = query.getResultList();
   }

   private Predicate[] getSearchPredicates(Root<Categoriaenlace> root)
   {

      CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
      List<Predicate> predicatesList = new ArrayList<Predicate>();

      String categoriaenlace = this.example.getCategoriaenlace();
      if (categoriaenlace != null && !"".equals(categoriaenlace))
      {
         predicatesList.add(builder.like(builder.lower(root.<String> get("categoriaenlace")), '%' + categoriaenlace.toLowerCase() + '%'));
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

   public List<Categoriaenlace> getPageItems()
   {
      return this.pageItems;
   }

   public long getCount()
   {
      return this.count;
   }

   /*
    * Support listing and POSTing back Categoriaenlace entities (e.g. from inside an
    * HtmlSelectOneMenu)
    */

   public List<Categoriaenlace> getAll()
   {

      CriteriaQuery<Categoriaenlace> criteria = this.entityManager
            .getCriteriaBuilder().createQuery(Categoriaenlace.class);
      return this.entityManager.createQuery(
            criteria.select(criteria.from(Categoriaenlace.class))).getResultList();
   }

   @Resource
   private SessionContext sessionContext;

   public Converter getConverter()
   {

      final CategoriaenlaceBean ejbProxy = this.sessionContext.getBusinessObject(CategoriaenlaceBean.class);

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

            return String.valueOf(((Categoriaenlace) value).getIdcategoriaenlace());
         }
      };
   }

   /*
    * Support adding children to bidirectional, one-to-many tables
    */

   private Categoriaenlace add = new Categoriaenlace();

   public Categoriaenlace getAdd()
   {
      return this.add;
   }

   public Categoriaenlace getAdded()
   {
      Categoriaenlace added = this.add;
      this.add = new Categoriaenlace();
      return added;
   }
}
