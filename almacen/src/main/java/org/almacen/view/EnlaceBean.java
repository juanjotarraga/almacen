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

import org.almacen.model.Enlace;

/**
 * Backing bean for Enlace entities.
 * <p/>
 * This class provides CRUD functionality for all Enlace entities. It focuses
 * purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt> for
 * state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD framework or
 * custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class EnlaceBean implements Serializable
{

   private static final long serialVersionUID = 1L;

   /*
    * Support creating and retrieving Enlace entities
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

   private Enlace enlace;

   public Enlace getEnlace()
   {
      return this.enlace;
   }

   public void setEnlace(Enlace enlace)
   {
      this.enlace = enlace;
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
         this.enlace = this.example;
      }
      else
      {
         this.enlace = findById(getId());
      }
   }

   public Enlace findById(Integer id)
   {

      return this.entityManager.find(Enlace.class, id);
   }

   /*
    * Support updating and deleting Enlace entities
    */

   public String update()
   {
      this.conversation.end();

      try
      {
         if (this.id == null)
         {
            this.entityManager.persist(this.enlace);
            return "search?faces-redirect=true";
         }
         else
         {
            this.entityManager.merge(this.enlace);
            return "view?faces-redirect=true&id=" + this.enlace.getIdenlace();
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
         Enlace deletableEntity = findById(getId());

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
    * Support searching Enlace entities with pagination
    */

   private int page;
   private long count;
   private List<Enlace> pageItems;

   private Enlace example = new Enlace();

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

   public Enlace getExample()
   {
      return this.example;
   }

   public void setExample(Enlace example)
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
      Root<Enlace> root = countCriteria.from(Enlace.class);
      countCriteria = countCriteria.select(builder.count(root)).where(
            getSearchPredicates(root));
      this.count = this.entityManager.createQuery(countCriteria)
            .getSingleResult();

      // Populate this.pageItems

      CriteriaQuery<Enlace> criteria = builder.createQuery(Enlace.class);
      root = criteria.from(Enlace.class);
      TypedQuery<Enlace> query = this.entityManager.createQuery(criteria
            .select(root).where(getSearchPredicates(root)));
      query.setFirstResult(this.page * getPageSize()).setMaxResults(
            getPageSize());
      this.pageItems = query.getResultList();
   }

   private Predicate[] getSearchPredicates(Root<Enlace> root)
   {

      CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
      List<Predicate> predicatesList = new ArrayList<Predicate>();

      String enlace = this.example.getEnlace();
      if (enlace != null && !"".equals(enlace))
      {
         predicatesList.add(builder.like(builder.lower(root.<String> get("enlace")), '%' + enlace.toLowerCase() + '%'));
      }
      String descripcion = this.example.getDescripcion();
      if (descripcion != null && !"".equals(descripcion))
      {
         predicatesList.add(builder.like(builder.lower(root.<String> get("descripcion")), '%' + descripcion.toLowerCase() + '%'));
      }
      Integer orden = this.example.getOrden();
      if (orden != null && orden.intValue() != 0)
      {
         predicatesList.add(builder.equal(root.get("orden"), orden));
      }
      String rutaenlace = this.example.getRutaenlace();
      if (rutaenlace != null && !"".equals(rutaenlace))
      {
         predicatesList.add(builder.like(builder.lower(root.<String> get("rutaenlace")), '%' + rutaenlace.toLowerCase() + '%'));
      }
      String imagen = this.example.getImagen();
      if (imagen != null && !"".equals(imagen))
      {
         predicatesList.add(builder.like(builder.lower(root.<String> get("imagen")), '%' + imagen.toLowerCase() + '%'));
      }

      return predicatesList.toArray(new Predicate[predicatesList.size()]);
   }

   public List<Enlace> getPageItems()
   {
      return this.pageItems;
   }

   public long getCount()
   {
      return this.count;
   }

   /*
    * Support listing and POSTing back Enlace entities (e.g. from inside an
    * HtmlSelectOneMenu)
    */

   public List<Enlace> getAll()
   {

      CriteriaQuery<Enlace> criteria = this.entityManager
            .getCriteriaBuilder().createQuery(Enlace.class);
      return this.entityManager.createQuery(
            criteria.select(criteria.from(Enlace.class))).getResultList();
   }

   @Resource
   private SessionContext sessionContext;

   public Converter getConverter()
   {

      final EnlaceBean ejbProxy = this.sessionContext.getBusinessObject(EnlaceBean.class);

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

            return String.valueOf(((Enlace) value).getIdenlace());
         }
      };
   }

   /*
    * Support adding children to bidirectional, one-to-many tables
    */

   private Enlace add = new Enlace();

   public Enlace getAdd()
   {
      return this.add;
   }

   public Enlace getAdded()
   {
      Enlace added = this.add;
      this.add = new Enlace();
      return added;
   }
}
