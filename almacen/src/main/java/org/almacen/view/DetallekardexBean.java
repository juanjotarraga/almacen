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

import org.almacen.model.Detallekardex;

/**
 * Backing bean for Detallekardex entities.
 * <p/>
 * This class provides CRUD functionality for all Detallekardex entities. It focuses
 * purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt> for
 * state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD framework or
 * custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class DetallekardexBean implements Serializable
{

   private static final long serialVersionUID = 1L;

   /*
    * Support creating and retrieving Detallekardex entities
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

   private Detallekardex detallekardex;

   public Detallekardex getDetallekardex()
   {
      return this.detallekardex;
   }

   public void setDetallekardex(Detallekardex detallekardex)
   {
      this.detallekardex = detallekardex;
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
         this.detallekardex = this.example;
      }
      else
      {
         this.detallekardex = findById(getId());
      }
   }

   public Detallekardex findById(Integer id)
   {

      return this.entityManager.find(Detallekardex.class, id);
   }

   /*
    * Support updating and deleting Detallekardex entities
    */

   public String update()
   {
      this.conversation.end();

      try
      {
         if (this.id == null)
         {
            this.entityManager.persist(this.detallekardex);
            return "search?faces-redirect=true";
         }
         else
         {
            this.entityManager.merge(this.detallekardex);
            return "view?faces-redirect=true&id=" + this.detallekardex.getIddetallekardex();
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
         Detallekardex deletableEntity = findById(getId());

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
    * Support searching Detallekardex entities with pagination
    */

   private int page;
   private long count;
   private List<Detallekardex> pageItems;

   private Detallekardex example = new Detallekardex();

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

   public Detallekardex getExample()
   {
      return this.example;
   }

   public void setExample(Detallekardex example)
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
      Root<Detallekardex> root = countCriteria.from(Detallekardex.class);
      countCriteria = countCriteria.select(builder.count(root)).where(
            getSearchPredicates(root));
      this.count = this.entityManager.createQuery(countCriteria)
            .getSingleResult();

      // Populate this.pageItems

      CriteriaQuery<Detallekardex> criteria = builder.createQuery(Detallekardex.class);
      root = criteria.from(Detallekardex.class);
      TypedQuery<Detallekardex> query = this.entityManager.createQuery(criteria
            .select(root).where(getSearchPredicates(root)));
      query.setFirstResult(this.page * getPageSize()).setMaxResults(
            getPageSize());
      this.pageItems = query.getResultList();
   }

   private Predicate[] getSearchPredicates(Root<Detallekardex> root)
   {

      CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
      List<Predicate> predicatesList = new ArrayList<Predicate>();

      String fechavencimiento = this.example.getFechavencimiento();
      if (fechavencimiento != null && !"".equals(fechavencimiento))
      {
         predicatesList.add(builder.like(builder.lower(root.<String> get("fechavencimiento")), '%' + fechavencimiento.toLowerCase() + '%'));
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
      Integer idkardex = this.example.getIdkardex();
      if (idkardex != null && idkardex.intValue() != 0)
      {
         predicatesList.add(builder.equal(root.get("idkardex"), idkardex));
      }
      Integer idestado = this.example.getIdestado();
      if (idestado != null && idestado.intValue() != 0)
      {
         predicatesList.add(builder.equal(root.get("idestado"), idestado));
      }

      return predicatesList.toArray(new Predicate[predicatesList.size()]);
   }

   public List<Detallekardex> getPageItems()
   {
      return this.pageItems;
   }

   public long getCount()
   {
      return this.count;
   }

   /*
    * Support listing and POSTing back Detallekardex entities (e.g. from inside an
    * HtmlSelectOneMenu)
    */

   public List<Detallekardex> getAll()
   {

      CriteriaQuery<Detallekardex> criteria = this.entityManager
            .getCriteriaBuilder().createQuery(Detallekardex.class);
      return this.entityManager.createQuery(
            criteria.select(criteria.from(Detallekardex.class))).getResultList();
   }

   @Resource
   private SessionContext sessionContext;

   public Converter getConverter()
   {

      final DetallekardexBean ejbProxy = this.sessionContext.getBusinessObject(DetallekardexBean.class);

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

            return String.valueOf(((Detallekardex) value).getIddetallekardex());
         }
      };
   }

   /*
    * Support adding children to bidirectional, one-to-many tables
    */

   private Detallekardex add = new Detallekardex();

   public Detallekardex getAdd()
   {
      return this.add;
   }

   public Detallekardex getAdded()
   {
      Detallekardex added = this.add;
      this.add = new Detallekardex();
      return added;
   }
}
