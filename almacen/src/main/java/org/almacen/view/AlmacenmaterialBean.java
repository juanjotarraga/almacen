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

import org.almacen.model.Almacenmaterial;

/**
 * Backing bean for Almacenmaterial entities.
 * <p/>
 * This class provides CRUD functionality for all Almacenmaterial entities. It focuses
 * purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt> for
 * state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD framework or
 * custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class AlmacenmaterialBean implements Serializable
{

   private static final long serialVersionUID = 1L;

   /*
    * Support creating and retrieving Almacenmaterial entities
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

   private Almacenmaterial almacenmaterial;

   public Almacenmaterial getAlmacenmaterial()
   {
      return this.almacenmaterial;
   }

   public void setAlmacenmaterial(Almacenmaterial almacenmaterial)
   {
      this.almacenmaterial = almacenmaterial;
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
         this.almacenmaterial = this.example;
      }
      else
      {
         this.almacenmaterial = findById(getId());
      }
   }

   public Almacenmaterial findById(Integer id)
   {

      return this.entityManager.find(Almacenmaterial.class, id);
   }

   /*
    * Support updating and deleting Almacenmaterial entities
    */

   public String update()
   {
      this.conversation.end();

      try
      {
         if (this.id == null)
         {
            this.entityManager.persist(this.almacenmaterial);
            return "search?faces-redirect=true";
         }
         else
         {
            this.entityManager.merge(this.almacenmaterial);
            return "view?faces-redirect=true&id=" + this.almacenmaterial.getIdalmacenmaterial();
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
         Almacenmaterial deletableEntity = findById(getId());

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
    * Support searching Almacenmaterial entities with pagination
    */

   private int page;
   private long count;
   private List<Almacenmaterial> pageItems;

   private Almacenmaterial example = new Almacenmaterial();

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

   public Almacenmaterial getExample()
   {
      return this.example;
   }

   public void setExample(Almacenmaterial example)
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
      Root<Almacenmaterial> root = countCriteria.from(Almacenmaterial.class);
      countCriteria = countCriteria.select(builder.count(root)).where(
            getSearchPredicates(root));
      this.count = this.entityManager.createQuery(countCriteria)
            .getSingleResult();

      // Populate this.pageItems

      CriteriaQuery<Almacenmaterial> criteria = builder.createQuery(Almacenmaterial.class);
      root = criteria.from(Almacenmaterial.class);
      TypedQuery<Almacenmaterial> query = this.entityManager.createQuery(criteria
            .select(root).where(getSearchPredicates(root)));
      query.setFirstResult(this.page * getPageSize()).setMaxResults(
            getPageSize());
      this.pageItems = query.getResultList();
   }

   private Predicate[] getSearchPredicates(Root<Almacenmaterial> root)
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
      Integer idmaterial = this.example.getIdmaterial();
      if (idmaterial != null && idmaterial.intValue() != 0)
      {
         predicatesList.add(builder.equal(root.get("idmaterial"), idmaterial));
      }
      Integer idestado = this.example.getIdestado();
      if (idestado != null && idestado.intValue() != 0)
      {
         predicatesList.add(builder.equal(root.get("idestado"), idestado));
      }

      return predicatesList.toArray(new Predicate[predicatesList.size()]);
   }

   public List<Almacenmaterial> getPageItems()
   {
      return this.pageItems;
   }

   public long getCount()
   {
      return this.count;
   }

   /*
    * Support listing and POSTing back Almacenmaterial entities (e.g. from inside an
    * HtmlSelectOneMenu)
    */

   public List<Almacenmaterial> getAll()
   {

      CriteriaQuery<Almacenmaterial> criteria = this.entityManager
            .getCriteriaBuilder().createQuery(Almacenmaterial.class);
      return this.entityManager.createQuery(
            criteria.select(criteria.from(Almacenmaterial.class))).getResultList();
   }

   @Resource
   private SessionContext sessionContext;

   public Converter getConverter()
   {

      final AlmacenmaterialBean ejbProxy = this.sessionContext.getBusinessObject(AlmacenmaterialBean.class);

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

            return String.valueOf(((Almacenmaterial) value).getIdalmacenmaterial());
         }
      };
   }

   /*
    * Support adding children to bidirectional, one-to-many tables
    */

   private Almacenmaterial add = new Almacenmaterial();

   public Almacenmaterial getAdd()
   {
      return this.add;
   }

   public Almacenmaterial getAdded()
   {
      Almacenmaterial added = this.add;
      this.add = new Almacenmaterial();
      return added;
   }
}
