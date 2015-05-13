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

import org.almacen.model.Usuariorol;

/**
 * Backing bean for Usuariorol entities.
 * <p/>
 * This class provides CRUD functionality for all Usuariorol entities. It focuses
 * purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt> for
 * state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD framework or
 * custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class UsuariorolBean implements Serializable
{

   private static final long serialVersionUID = 1L;

   /*
    * Support creating and retrieving Usuariorol entities
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

   private Usuariorol usuariorol;

   public Usuariorol getUsuariorol()
   {
      return this.usuariorol;
   }

   public void setUsuariorol(Usuariorol usuariorol)
   {
      this.usuariorol = usuariorol;
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
         this.usuariorol = this.example;
      }
      else
      {
         this.usuariorol = findById(getId());
      }
   }

   public Usuariorol findById(Integer id)
   {

      return this.entityManager.find(Usuariorol.class, id);
   }

   /*
    * Support updating and deleting Usuariorol entities
    */

   public String update()
   {
      this.conversation.end();

      try
      {
         if (this.id == null)
         {
            this.entityManager.persist(this.usuariorol);
            return "search?faces-redirect=true";
         }
         else
         {
            this.entityManager.merge(this.usuariorol);
            return "view?faces-redirect=true&id=" + this.usuariorol.getIdusuariorol();
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
         Usuariorol deletableEntity = findById(getId());

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
    * Support searching Usuariorol entities with pagination
    */

   private int page;
   private long count;
   private List<Usuariorol> pageItems;

   private Usuariorol example = new Usuariorol();

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

   public Usuariorol getExample()
   {
      return this.example;
   }

   public void setExample(Usuariorol example)
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
      Root<Usuariorol> root = countCriteria.from(Usuariorol.class);
      countCriteria = countCriteria.select(builder.count(root)).where(
            getSearchPredicates(root));
      this.count = this.entityManager.createQuery(countCriteria)
            .getSingleResult();

      // Populate this.pageItems

      CriteriaQuery<Usuariorol> criteria = builder.createQuery(Usuariorol.class);
      root = criteria.from(Usuariorol.class);
      TypedQuery<Usuariorol> query = this.entityManager.createQuery(criteria
            .select(root).where(getSearchPredicates(root)));
      query.setFirstResult(this.page * getPageSize()).setMaxResults(
            getPageSize());
      this.pageItems = query.getResultList();
   }

   private Predicate[] getSearchPredicates(Root<Usuariorol> root)
   {

      CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
      List<Predicate> predicatesList = new ArrayList<Predicate>();

      String fechaexpiracion = this.example.getFechaexpiracion();
      if (fechaexpiracion != null && !"".equals(fechaexpiracion))
      {
         predicatesList.add(builder.like(builder.lower(root.<String> get("fechaexpiracion")), '%' + fechaexpiracion.toLowerCase() + '%'));
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
      Integer idrol = this.example.getIdrol();
      if (idrol != null && idrol.intValue() != 0)
      {
         predicatesList.add(builder.equal(root.get("idrol"), idrol));
      }
      Integer idusuario = this.example.getIdusuario();
      if (idusuario != null && idusuario.intValue() != 0)
      {
         predicatesList.add(builder.equal(root.get("idusuario"), idusuario));
      }

      return predicatesList.toArray(new Predicate[predicatesList.size()]);
   }

   public List<Usuariorol> getPageItems()
   {
      return this.pageItems;
   }

   public long getCount()
   {
      return this.count;
   }

   /*
    * Support listing and POSTing back Usuariorol entities (e.g. from inside an
    * HtmlSelectOneMenu)
    */

   public List<Usuariorol> getAll()
   {

      CriteriaQuery<Usuariorol> criteria = this.entityManager
            .getCriteriaBuilder().createQuery(Usuariorol.class);
      return this.entityManager.createQuery(
            criteria.select(criteria.from(Usuariorol.class))).getResultList();
   }

   @Resource
   private SessionContext sessionContext;

   public Converter getConverter()
   {

      final UsuariorolBean ejbProxy = this.sessionContext.getBusinessObject(UsuariorolBean.class);

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

            return String.valueOf(((Usuariorol) value).getIdusuariorol());
         }
      };
   }

   /*
    * Support adding children to bidirectional, one-to-many tables
    */

   private Usuariorol add = new Usuariorol();

   public Usuariorol getAdd()
   {
      return this.add;
   }

   public Usuariorol getAdded()
   {
      Usuariorol added = this.add;
      this.add = new Usuariorol();
      return added;
   }
}
