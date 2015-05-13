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

import org.almacen.model.Solicitud;

/**
 * Backing bean for Solicitud entities.
 * <p/>
 * This class provides CRUD functionality for all Solicitud entities. It focuses
 * purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt> for
 * state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD framework or
 * custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class SolicitudBean implements Serializable
{

   private static final long serialVersionUID = 1L;

   /*
    * Support creating and retrieving Solicitud entities
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

   private Solicitud solicitud;

   public Solicitud getSolicitud()
   {
      return this.solicitud;
   }

   public void setSolicitud(Solicitud solicitud)
   {
      this.solicitud = solicitud;
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
         this.solicitud = this.example;
      }
      else
      {
         this.solicitud = findById(getId());
      }
   }

   public Solicitud findById(Integer id)
   {

      return this.entityManager.find(Solicitud.class, id);
   }

   /*
    * Support updating and deleting Solicitud entities
    */

   public String update()
   {
      this.conversation.end();

      try
      {
         if (this.id == null)
         {
            this.entityManager.persist(this.solicitud);
            return "search?faces-redirect=true";
         }
         else
         {
            this.entityManager.merge(this.solicitud);
            return "view?faces-redirect=true&id=" + this.solicitud.getIdsolicitud();
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
         Solicitud deletableEntity = findById(getId());

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
    * Support searching Solicitud entities with pagination
    */

   private int page;
   private long count;
   private List<Solicitud> pageItems;

   private Solicitud example = new Solicitud();

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

   public Solicitud getExample()
   {
      return this.example;
   }

   public void setExample(Solicitud example)
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
      Root<Solicitud> root = countCriteria.from(Solicitud.class);
      countCriteria = countCriteria.select(builder.count(root)).where(
            getSearchPredicates(root));
      this.count = this.entityManager.createQuery(countCriteria)
            .getSingleResult();

      // Populate this.pageItems

      CriteriaQuery<Solicitud> criteria = builder.createQuery(Solicitud.class);
      root = criteria.from(Solicitud.class);
      TypedQuery<Solicitud> query = this.entityManager.createQuery(criteria
            .select(root).where(getSearchPredicates(root)));
      query.setFirstResult(this.page * getPageSize()).setMaxResults(
            getPageSize());
      this.pageItems = query.getResultList();
   }

   private Predicate[] getSearchPredicates(Root<Solicitud> root)
   {

      CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
      List<Predicate> predicatesList = new ArrayList<Predicate>();

      String solicitud = this.example.getSolicitud();
      if (solicitud != null && !"".equals(solicitud))
      {
         predicatesList.add(builder.like(builder.lower(root.<String> get("solicitud")), '%' + solicitud.toLowerCase() + '%'));
      }
      String codigosolicitud = this.example.getCodigosolicitud();
      if (codigosolicitud != null && !"".equals(codigosolicitud))
      {
         predicatesList.add(builder.like(builder.lower(root.<String> get("codigosolicitud")), '%' + codigosolicitud.toLowerCase() + '%'));
      }
      String fechasolicitud = this.example.getFechasolicitud();
      if (fechasolicitud != null && !"".equals(fechasolicitud))
      {
         predicatesList.add(builder.like(builder.lower(root.<String> get("fechasolicitud")), '%' + fechasolicitud.toLowerCase() + '%'));
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

      return predicatesList.toArray(new Predicate[predicatesList.size()]);
   }

   public List<Solicitud> getPageItems()
   {
      return this.pageItems;
   }

   public long getCount()
   {
      return this.count;
   }

   /*
    * Support listing and POSTing back Solicitud entities (e.g. from inside an
    * HtmlSelectOneMenu)
    */

   public List<Solicitud> getAll()
   {

      CriteriaQuery<Solicitud> criteria = this.entityManager
            .getCriteriaBuilder().createQuery(Solicitud.class);
      return this.entityManager.createQuery(
            criteria.select(criteria.from(Solicitud.class))).getResultList();
   }

   @Resource
   private SessionContext sessionContext;

   public Converter getConverter()
   {

      final SolicitudBean ejbProxy = this.sessionContext.getBusinessObject(SolicitudBean.class);

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

            return String.valueOf(((Solicitud) value).getIdsolicitud());
         }
      };
   }

   /*
    * Support adding children to bidirectional, one-to-many tables
    */

   private Solicitud add = new Solicitud();

   public Solicitud getAdd()
   {
      return this.add;
   }

   public Solicitud getAdded()
   {
      Solicitud added = this.add;
      this.add = new Solicitud();
      return added;
   }
}
