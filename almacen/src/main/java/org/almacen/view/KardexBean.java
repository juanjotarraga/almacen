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

import org.almacen.model.Kardex;

/**
 * Backing bean for Kardex entities.
 * <p/>
 * This class provides CRUD functionality for all Kardex entities. It focuses
 * purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt> for
 * state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD framework or
 * custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class KardexBean implements Serializable
{

   private static final long serialVersionUID = 1L;

   /*
    * Support creating and retrieving Kardex entities
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

   private Kardex kardex;

   public Kardex getKardex()
   {
      return this.kardex;
   }

   public void setKardex(Kardex kardex)
   {
      this.kardex = kardex;
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
         this.kardex = this.example;
      }
      else
      {
         this.kardex = findById(getId());
      }
   }

   public Kardex findById(Integer id)
   {

      return this.entityManager.find(Kardex.class, id);
   }

   /*
    * Support updating and deleting Kardex entities
    */

   public String update()
   {
      this.conversation.end();

      try
      {
         if (this.id == null)
         {
            this.entityManager.persist(this.kardex);
            return "search?faces-redirect=true";
         }
         else
         {
            this.entityManager.merge(this.kardex);
            return "view?faces-redirect=true&id=" + this.kardex.getIdkardex();
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
         Kardex deletableEntity = findById(getId());

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
    * Support searching Kardex entities with pagination
    */

   private int page;
   private long count;
   private List<Kardex> pageItems;

   private Kardex example = new Kardex();

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

   public Kardex getExample()
   {
      return this.example;
   }

   public void setExample(Kardex example)
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
      Root<Kardex> root = countCriteria.from(Kardex.class);
      countCriteria = countCriteria.select(builder.count(root)).where(
            getSearchPredicates(root));
      this.count = this.entityManager.createQuery(countCriteria)
            .getSingleResult();

      // Populate this.pageItems

      CriteriaQuery<Kardex> criteria = builder.createQuery(Kardex.class);
      root = criteria.from(Kardex.class);
      TypedQuery<Kardex> query = this.entityManager.createQuery(criteria
            .select(root).where(getSearchPredicates(root)));
      query.setFirstResult(this.page * getPageSize()).setMaxResults(
            getPageSize());
      this.pageItems = query.getResultList();
   }

   private Predicate[] getSearchPredicates(Root<Kardex> root)
   {

      CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
      List<Predicate> predicatesList = new ArrayList<Predicate>();

      String descripcion = this.example.getDescripcion();
      if (descripcion != null && !"".equals(descripcion))
      {
         predicatesList.add(builder.like(builder.lower(root.<String> get("descripcion")), '%' + descripcion.toLowerCase() + '%'));
      }
      String nroestante = this.example.getNroestante();
      if (nroestante != null && !"".equals(nroestante))
      {
         predicatesList.add(builder.like(builder.lower(root.<String> get("nroestante")), '%' + nroestante.toLowerCase() + '%'));
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
      Integer idalmacenmaterial = this.example.getIdalmacenmaterial();
      if (idalmacenmaterial != null && idalmacenmaterial.intValue() != 0)
      {
         predicatesList.add(builder.equal(root.get("idalmacenmaterial"), idalmacenmaterial));
      }

      return predicatesList.toArray(new Predicate[predicatesList.size()]);
   }

   public List<Kardex> getPageItems()
   {
      return this.pageItems;
   }

   public long getCount()
   {
      return this.count;
   }

   /*
    * Support listing and POSTing back Kardex entities (e.g. from inside an
    * HtmlSelectOneMenu)
    */

   public List<Kardex> getAll()
   {

      CriteriaQuery<Kardex> criteria = this.entityManager
            .getCriteriaBuilder().createQuery(Kardex.class);
      return this.entityManager.createQuery(
            criteria.select(criteria.from(Kardex.class))).getResultList();
   }

   @Resource
   private SessionContext sessionContext;

   public Converter getConverter()
   {

      final KardexBean ejbProxy = this.sessionContext.getBusinessObject(KardexBean.class);

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

            return String.valueOf(((Kardex) value).getIdkardex());
         }
      };
   }

   /*
    * Support adding children to bidirectional, one-to-many tables
    */

   private Kardex add = new Kardex();

   public Kardex getAdd()
   {
      return this.add;
   }

   public Kardex getAdded()
   {
      Kardex added = this.add;
      this.add = new Kardex();
      return added;
   }
}
