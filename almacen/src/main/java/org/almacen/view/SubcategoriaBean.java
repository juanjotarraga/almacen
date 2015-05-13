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

import org.almacen.model.Subcategoria;

/**
 * Backing bean for Subcategoria entities.
 * <p/>
 * This class provides CRUD functionality for all Subcategoria entities. It focuses
 * purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt> for
 * state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD framework or
 * custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class SubcategoriaBean implements Serializable
{

   private static final long serialVersionUID = 1L;

   /*
    * Support creating and retrieving Subcategoria entities
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

   private Subcategoria subcategoria;

   public Subcategoria getSubcategoria()
   {
      return this.subcategoria;
   }

   public void setSubcategoria(Subcategoria subcategoria)
   {
      this.subcategoria = subcategoria;
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
         this.subcategoria = this.example;
      }
      else
      {
         this.subcategoria = findById(getId());
      }
   }

   public Subcategoria findById(Integer id)
   {

      return this.entityManager.find(Subcategoria.class, id);
   }

   /*
    * Support updating and deleting Subcategoria entities
    */

   public String update()
   {
      this.conversation.end();

      try
      {
         if (this.id == null)
         {
            this.entityManager.persist(this.subcategoria);
            return "search?faces-redirect=true";
         }
         else
         {
            this.entityManager.merge(this.subcategoria);
            return "view?faces-redirect=true&id=" + this.subcategoria.getIdsubcategoria();
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
         Subcategoria deletableEntity = findById(getId());

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
    * Support searching Subcategoria entities with pagination
    */

   private int page;
   private long count;
   private List<Subcategoria> pageItems;

   private Subcategoria example = new Subcategoria();

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

   public Subcategoria getExample()
   {
      return this.example;
   }

   public void setExample(Subcategoria example)
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
      Root<Subcategoria> root = countCriteria.from(Subcategoria.class);
      countCriteria = countCriteria.select(builder.count(root)).where(
            getSearchPredicates(root));
      this.count = this.entityManager.createQuery(countCriteria)
            .getSingleResult();

      // Populate this.pageItems

      CriteriaQuery<Subcategoria> criteria = builder.createQuery(Subcategoria.class);
      root = criteria.from(Subcategoria.class);
      TypedQuery<Subcategoria> query = this.entityManager.createQuery(criteria
            .select(root).where(getSearchPredicates(root)));
      query.setFirstResult(this.page * getPageSize()).setMaxResults(
            getPageSize());
      this.pageItems = query.getResultList();
   }

   private Predicate[] getSearchPredicates(Root<Subcategoria> root)
   {

      CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
      List<Predicate> predicatesList = new ArrayList<Predicate>();

      String subcategoria = this.example.getSubcategoria();
      if (subcategoria != null && !"".equals(subcategoria))
      {
         predicatesList.add(builder.like(builder.lower(root.<String> get("subcategoria")), '%' + subcategoria.toLowerCase() + '%'));
      }
      String codigosubcategoria = this.example.getCodigosubcategoria();
      if (codigosubcategoria != null && !"".equals(codigosubcategoria))
      {
         predicatesList.add(builder.like(builder.lower(root.<String> get("codigosubcategoria")), '%' + codigosubcategoria.toLowerCase() + '%'));
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

      return predicatesList.toArray(new Predicate[predicatesList.size()]);
   }

   public List<Subcategoria> getPageItems()
   {
      return this.pageItems;
   }

   public long getCount()
   {
      return this.count;
   }

   /*
    * Support listing and POSTing back Subcategoria entities (e.g. from inside an
    * HtmlSelectOneMenu)
    */

   public List<Subcategoria> getAll()
   {

      CriteriaQuery<Subcategoria> criteria = this.entityManager
            .getCriteriaBuilder().createQuery(Subcategoria.class);
      return this.entityManager.createQuery(
            criteria.select(criteria.from(Subcategoria.class))).getResultList();
   }

   @Resource
   private SessionContext sessionContext;

   public Converter getConverter()
   {

      final SubcategoriaBean ejbProxy = this.sessionContext.getBusinessObject(SubcategoriaBean.class);

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

            return String.valueOf(((Subcategoria) value).getIdsubcategoria());
         }
      };
   }

   /*
    * Support adding children to bidirectional, one-to-many tables
    */

   private Subcategoria add = new Subcategoria();

   public Subcategoria getAdd()
   {
      return this.add;
   }

   public Subcategoria getAdded()
   {
      Subcategoria added = this.add;
      this.add = new Subcategoria();
      return added;
   }
}
