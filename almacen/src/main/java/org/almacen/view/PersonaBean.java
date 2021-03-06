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

import org.almacen.model.Persona;

/**
 * Backing bean for Persona entities.
 * <p/>
 * This class provides CRUD functionality for all Persona entities. It focuses
 * purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt> for
 * state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD framework or
 * custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class PersonaBean implements Serializable
{

   private static final long serialVersionUID = 1L;

   /*
    * Support creating and retrieving Persona entities
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

   private Persona persona;

   public Persona getPersona()
   {
      return this.persona;
   }

   public void setPersona(Persona persona)
   {
      this.persona = persona;
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
         this.persona = this.example;
      }
      else
      {
         this.persona = findById(getId());
      }
   }

   public Persona findById(Integer id)
   {

      return this.entityManager.find(Persona.class, id);
   }

   /*
    * Support updating and deleting Persona entities
    */

   public String update()
   {
      this.conversation.end();

      try
      {
         if (this.id == null)
         {
            this.entityManager.persist(this.persona);
            return "search?faces-redirect=true";
         }
         else
         {
            this.entityManager.merge(this.persona);
            return "view?faces-redirect=true&id=" + this.persona.getIdpersona();
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
         Persona deletableEntity = findById(getId());

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
    * Support searching Persona entities with pagination
    */

   private int page;
   private long count;
   private List<Persona> pageItems;

   private Persona example = new Persona();

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

   public Persona getExample()
   {
      return this.example;
   }

   public void setExample(Persona example)
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
      Root<Persona> root = countCriteria.from(Persona.class);
      countCriteria = countCriteria.select(builder.count(root)).where(
            getSearchPredicates(root));
      this.count = this.entityManager.createQuery(countCriteria)
            .getSingleResult();

      // Populate this.pageItems

      CriteriaQuery<Persona> criteria = builder.createQuery(Persona.class);
      root = criteria.from(Persona.class);
      TypedQuery<Persona> query = this.entityManager.createQuery(criteria
            .select(root).where(getSearchPredicates(root)));
      query.setFirstResult(this.page * getPageSize()).setMaxResults(
            getPageSize());
      this.pageItems = query.getResultList();
   }

   private Predicate[] getSearchPredicates(Root<Persona> root)
   {

      CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
      List<Predicate> predicatesList = new ArrayList<Predicate>();

      String nombrepersona = this.example.getNombrepersona();
      if (nombrepersona != null && !"".equals(nombrepersona))
      {
         predicatesList.add(builder.like(builder.lower(root.<String> get("nombrepersona")), '%' + nombrepersona.toLowerCase() + '%'));
      }
      String apellidopaterno = this.example.getApellidopaterno();
      if (apellidopaterno != null && !"".equals(apellidopaterno))
      {
         predicatesList.add(builder.like(builder.lower(root.<String> get("apellidopaterno")), '%' + apellidopaterno.toLowerCase() + '%'));
      }
      String apellidomaterno = this.example.getApellidomaterno();
      if (apellidomaterno != null && !"".equals(apellidomaterno))
      {
         predicatesList.add(builder.like(builder.lower(root.<String> get("apellidomaterno")), '%' + apellidomaterno.toLowerCase() + '%'));
      }
      String ci = this.example.getCi();
      if (ci != null && !"".equals(ci))
      {
         predicatesList.add(builder.like(builder.lower(root.<String> get("ci")), '%' + ci.toLowerCase() + '%'));
      }
      String fechanacimiento = this.example.getFechanacimiento();
      if (fechanacimiento != null && !"".equals(fechanacimiento))
      {
         predicatesList.add(builder.like(builder.lower(root.<String> get("fechanacimiento")), '%' + fechanacimiento.toLowerCase() + '%'));
      }

      return predicatesList.toArray(new Predicate[predicatesList.size()]);
   }

   public List<Persona> getPageItems()
   {
      return this.pageItems;
   }

   public long getCount()
   {
      return this.count;
   }

   /*
    * Support listing and POSTing back Persona entities (e.g. from inside an
    * HtmlSelectOneMenu)
    */

   public List<Persona> getAll()
   {

      CriteriaQuery<Persona> criteria = this.entityManager
            .getCriteriaBuilder().createQuery(Persona.class);
      return this.entityManager.createQuery(
            criteria.select(criteria.from(Persona.class))).getResultList();
   }

   @Resource
   private SessionContext sessionContext;

   public Converter getConverter()
   {

      final PersonaBean ejbProxy = this.sessionContext.getBusinessObject(PersonaBean.class);

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

            return String.valueOf(((Persona) value).getIdpersona());
         }
      };
   }

   /*
    * Support adding children to bidirectional, one-to-many tables
    */

   private Persona add = new Persona();

   public Persona getAdd()
   {
      return this.add;
   }

   public Persona getAdded()
   {
      Persona added = this.add;
      this.add = new Persona();
      return added;
   }
}
