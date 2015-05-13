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

import org.almacen.model.Usuarioalmacen;

/**
 * Backing bean for Usuarioalmacen entities.
 * <p/>
 * This class provides CRUD functionality for all Usuarioalmacen entities. It focuses
 * purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt> for
 * state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD framework or
 * custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class UsuarioalmacenBean implements Serializable
{

   private static final long serialVersionUID = 1L;

   /*
    * Support creating and retrieving Usuarioalmacen entities
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

   private Usuarioalmacen usuarioalmacen;

   public Usuarioalmacen getUsuarioalmacen()
   {
      return this.usuarioalmacen;
   }

   public void setUsuarioalmacen(Usuarioalmacen usuarioalmacen)
   {
      this.usuarioalmacen = usuarioalmacen;
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
         this.usuarioalmacen = this.example;
      }
      else
      {
         this.usuarioalmacen = findById(getId());
      }
   }

   public Usuarioalmacen findById(Integer id)
   {

      return this.entityManager.find(Usuarioalmacen.class, id);
   }

   /*
    * Support updating and deleting Usuarioalmacen entities
    */

   public String update()
   {
      this.conversation.end();

      try
      {
         if (this.id == null)
         {
            this.entityManager.persist(this.usuarioalmacen);
            return "search?faces-redirect=true";
         }
         else
         {
            this.entityManager.merge(this.usuarioalmacen);
            return "view?faces-redirect=true&id=" + this.usuarioalmacen.getIdusuarioalmacen();
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
         Usuarioalmacen deletableEntity = findById(getId());

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
    * Support searching Usuarioalmacen entities with pagination
    */

   private int page;
   private long count;
   private List<Usuarioalmacen> pageItems;

   private Usuarioalmacen example = new Usuarioalmacen();

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

   public Usuarioalmacen getExample()
   {
      return this.example;
   }

   public void setExample(Usuarioalmacen example)
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
      Root<Usuarioalmacen> root = countCriteria.from(Usuarioalmacen.class);
      countCriteria = countCriteria.select(builder.count(root)).where(
            getSearchPredicates(root));
      this.count = this.entityManager.createQuery(countCriteria)
            .getSingleResult();

      // Populate this.pageItems

      CriteriaQuery<Usuarioalmacen> criteria = builder.createQuery(Usuarioalmacen.class);
      root = criteria.from(Usuarioalmacen.class);
      TypedQuery<Usuarioalmacen> query = this.entityManager.createQuery(criteria
            .select(root).where(getSearchPredicates(root)));
      query.setFirstResult(this.page * getPageSize()).setMaxResults(
            getPageSize());
      this.pageItems = query.getResultList();
   }

   private Predicate[] getSearchPredicates(Root<Usuarioalmacen> root)
   {

      CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
      List<Predicate> predicatesList = new ArrayList<Predicate>();

      String fechaasignacion = this.example.getFechaasignacion();
      if (fechaasignacion != null && !"".equals(fechaasignacion))
      {
         predicatesList.add(builder.like(builder.lower(root.<String> get("fechaasignacion")), '%' + fechaasignacion.toLowerCase() + '%'));
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
      Integer idalmacen = this.example.getIdalmacen();
      if (idalmacen != null && idalmacen.intValue() != 0)
      {
         predicatesList.add(builder.equal(root.get("idalmacen"), idalmacen));
      }
      Integer idusuario = this.example.getIdusuario();
      if (idusuario != null && idusuario.intValue() != 0)
      {
         predicatesList.add(builder.equal(root.get("idusuario"), idusuario));
      }

      return predicatesList.toArray(new Predicate[predicatesList.size()]);
   }

   public List<Usuarioalmacen> getPageItems()
   {
      return this.pageItems;
   }

   public long getCount()
   {
      return this.count;
   }

   /*
    * Support listing and POSTing back Usuarioalmacen entities (e.g. from inside an
    * HtmlSelectOneMenu)
    */

   public List<Usuarioalmacen> getAll()
   {

      CriteriaQuery<Usuarioalmacen> criteria = this.entityManager
            .getCriteriaBuilder().createQuery(Usuarioalmacen.class);
      return this.entityManager.createQuery(
            criteria.select(criteria.from(Usuarioalmacen.class))).getResultList();
   }

   @Resource
   private SessionContext sessionContext;

   public Converter getConverter()
   {

      final UsuarioalmacenBean ejbProxy = this.sessionContext.getBusinessObject(UsuarioalmacenBean.class);

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

            return String.valueOf(((Usuarioalmacen) value).getIdusuarioalmacen());
         }
      };
   }

   /*
    * Support adding children to bidirectional, one-to-many tables
    */

   private Usuarioalmacen add = new Usuarioalmacen();

   public Usuarioalmacen getAdd()
   {
      return this.add;
   }

   public Usuarioalmacen getAdded()
   {
      Usuarioalmacen added = this.add;
      this.add = new Usuarioalmacen();
      return added;
   }
}
