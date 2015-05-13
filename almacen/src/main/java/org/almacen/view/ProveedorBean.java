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

import org.almacen.model.Proveedor;

/**
 * Backing bean for Proveedor entities.
 * <p/>
 * This class provides CRUD functionality for all Proveedor entities. It focuses
 * purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt> for
 * state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD framework or
 * custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class ProveedorBean implements Serializable
{

   private static final long serialVersionUID = 1L;

   /*
    * Support creating and retrieving Proveedor entities
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

   private Proveedor proveedor;

   public Proveedor getProveedor()
   {
      return this.proveedor;
   }

   public void setProveedor(Proveedor proveedor)
   {
      this.proveedor = proveedor;
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
         this.proveedor = this.example;
      }
      else
      {
         this.proveedor = findById(getId());
      }
   }

   public Proveedor findById(Integer id)
   {

      return this.entityManager.find(Proveedor.class, id);
   }

   /*
    * Support updating and deleting Proveedor entities
    */

   public String update()
   {
      this.conversation.end();

      try
      {
         if (this.id == null)
         {
            this.entityManager.persist(this.proveedor);
            return "search?faces-redirect=true";
         }
         else
         {
            this.entityManager.merge(this.proveedor);
            return "view?faces-redirect=true&id=" + this.proveedor.getIdproveedor();
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
         Proveedor deletableEntity = findById(getId());

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
    * Support searching Proveedor entities with pagination
    */

   private int page;
   private long count;
   private List<Proveedor> pageItems;

   private Proveedor example = new Proveedor();

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

   public Proveedor getExample()
   {
      return this.example;
   }

   public void setExample(Proveedor example)
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
      Root<Proveedor> root = countCriteria.from(Proveedor.class);
      countCriteria = countCriteria.select(builder.count(root)).where(
            getSearchPredicates(root));
      this.count = this.entityManager.createQuery(countCriteria)
            .getSingleResult();

      // Populate this.pageItems

      CriteriaQuery<Proveedor> criteria = builder.createQuery(Proveedor.class);
      root = criteria.from(Proveedor.class);
      TypedQuery<Proveedor> query = this.entityManager.createQuery(criteria
            .select(root).where(getSearchPredicates(root)));
      query.setFirstResult(this.page * getPageSize()).setMaxResults(
            getPageSize());
      this.pageItems = query.getResultList();
   }

   private Predicate[] getSearchPredicates(Root<Proveedor> root)
   {

      CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
      List<Predicate> predicatesList = new ArrayList<Predicate>();

      String proveedor = this.example.getProveedor();
      if (proveedor != null && !"".equals(proveedor))
      {
         predicatesList.add(builder.like(builder.lower(root.<String> get("proveedor")), '%' + proveedor.toLowerCase() + '%'));
      }
      String responsable = this.example.getResponsable();
      if (responsable != null && !"".equals(responsable))
      {
         predicatesList.add(builder.like(builder.lower(root.<String> get("responsable")), '%' + responsable.toLowerCase() + '%'));
      }
      String telefono = this.example.getTelefono();
      if (telefono != null && !"".equals(telefono))
      {
         predicatesList.add(builder.like(builder.lower(root.<String> get("telefono")), '%' + telefono.toLowerCase() + '%'));
      }
      String email = this.example.getEmail();
      if (email != null && !"".equals(email))
      {
         predicatesList.add(builder.like(builder.lower(root.<String> get("email")), '%' + email.toLowerCase() + '%'));
      }
      String direccion = this.example.getDireccion();
      if (direccion != null && !"".equals(direccion))
      {
         predicatesList.add(builder.like(builder.lower(root.<String> get("direccion")), '%' + direccion.toLowerCase() + '%'));
      }

      return predicatesList.toArray(new Predicate[predicatesList.size()]);
   }

   public List<Proveedor> getPageItems()
   {
      return this.pageItems;
   }

   public long getCount()
   {
      return this.count;
   }

   /*
    * Support listing and POSTing back Proveedor entities (e.g. from inside an
    * HtmlSelectOneMenu)
    */

   public List<Proveedor> getAll()
   {

      CriteriaQuery<Proveedor> criteria = this.entityManager
            .getCriteriaBuilder().createQuery(Proveedor.class);
      return this.entityManager.createQuery(
            criteria.select(criteria.from(Proveedor.class))).getResultList();
   }

   @Resource
   private SessionContext sessionContext;

   public Converter getConverter()
   {

      final ProveedorBean ejbProxy = this.sessionContext.getBusinessObject(ProveedorBean.class);

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

            return String.valueOf(((Proveedor) value).getIdproveedor());
         }
      };
   }

   /*
    * Support adding children to bidirectional, one-to-many tables
    */

   private Proveedor add = new Proveedor();

   public Proveedor getAdd()
   {
      return this.add;
   }

   public Proveedor getAdded()
   {
      Proveedor added = this.add;
      this.add = new Proveedor();
      return added;
   }
}
