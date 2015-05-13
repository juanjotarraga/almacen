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

import org.almacen.model.Tiposolicitud;

/**
 * Backing bean for Tiposolicitud entities.
 * <p/>
 * This class provides CRUD functionality for all Tiposolicitud entities. It focuses
 * purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt> for
 * state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD framework or
 * custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class TiposolicitudBean implements Serializable
{

   private static final long serialVersionUID = 1L;

   /*
    * Support creating and retrieving Tiposolicitud entities
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

   private Tiposolicitud tiposolicitud;

   public Tiposolicitud getTiposolicitud()
   {
      return this.tiposolicitud;
   }

   public void setTiposolicitud(Tiposolicitud tiposolicitud)
   {
      this.tiposolicitud = tiposolicitud;
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
         this.tiposolicitud = this.example;
      }
      else
      {
         this.tiposolicitud = findById(getId());
      }
   }

   public Tiposolicitud findById(Integer id)
   {

      return this.entityManager.find(Tiposolicitud.class, id);
   }

   /*
    * Support updating and deleting Tiposolicitud entities
    */

   public String update()
   {
      this.conversation.end();

      try
      {
         if (this.id == null)
         {
            this.entityManager.persist(this.tiposolicitud);
            return "search?faces-redirect=true";
         }
         else
         {
            this.entityManager.merge(this.tiposolicitud);
            return "view?faces-redirect=true&id=" + this.tiposolicitud.getIdtiposolicitud();
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
         Tiposolicitud deletableEntity = findById(getId());

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
    * Support searching Tiposolicitud entities with pagination
    */

   private int page;
   private long count;
   private List<Tiposolicitud> pageItems;

   private Tiposolicitud example = new Tiposolicitud();

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

   public Tiposolicitud getExample()
   {
      return this.example;
   }

   public void setExample(Tiposolicitud example)
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
      Root<Tiposolicitud> root = countCriteria.from(Tiposolicitud.class);
      countCriteria = countCriteria.select(builder.count(root)).where(
            getSearchPredicates(root));
      this.count = this.entityManager.createQuery(countCriteria)
            .getSingleResult();

      // Populate this.pageItems

      CriteriaQuery<Tiposolicitud> criteria = builder.createQuery(Tiposolicitud.class);
      root = criteria.from(Tiposolicitud.class);
      TypedQuery<Tiposolicitud> query = this.entityManager.createQuery(criteria
            .select(root).where(getSearchPredicates(root)));
      query.setFirstResult(this.page * getPageSize()).setMaxResults(
            getPageSize());
      this.pageItems = query.getResultList();
   }

   private Predicate[] getSearchPredicates(Root<Tiposolicitud> root)
   {

      CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
      List<Predicate> predicatesList = new ArrayList<Predicate>();

      String tiposolicitud = this.example.getTiposolicitud();
      if (tiposolicitud != null && !"".equals(tiposolicitud))
      {
         predicatesList.add(builder.like(builder.lower(root.<String> get("tiposolicitud")), '%' + tiposolicitud.toLowerCase() + '%'));
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

   public List<Tiposolicitud> getPageItems()
   {
      return this.pageItems;
   }

   public long getCount()
   {
      return this.count;
   }

   /*
    * Support listing and POSTing back Tiposolicitud entities (e.g. from inside an
    * HtmlSelectOneMenu)
    */

   public List<Tiposolicitud> getAll()
   {

      CriteriaQuery<Tiposolicitud> criteria = this.entityManager
            .getCriteriaBuilder().createQuery(Tiposolicitud.class);
      return this.entityManager.createQuery(
            criteria.select(criteria.from(Tiposolicitud.class))).getResultList();
   }

   @Resource
   private SessionContext sessionContext;

   public Converter getConverter()
   {

      final TiposolicitudBean ejbProxy = this.sessionContext.getBusinessObject(TiposolicitudBean.class);

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

            return String.valueOf(((Tiposolicitud) value).getIdtiposolicitud());
         }
      };
   }

   /*
    * Support adding children to bidirectional, one-to-many tables
    */

   private Tiposolicitud add = new Tiposolicitud();

   public Tiposolicitud getAdd()
   {
      return this.add;
   }

   public Tiposolicitud getAdded()
   {
      Tiposolicitud added = this.add;
      this.add = new Tiposolicitud();
      return added;
   }
}
