AUTO CRUD
===
_AutoCrud is an application that manages the CRUD operations of an class and generate a GUI JavaFx **Automatically** . All you need to do is create the model class* and pass it as parametre._

*AutoCrud version 1.1.0 support (Date, Collection and Entity of other class).

Steps to add a class model to the application
---

1. Create your Class in `src\com\ofppt\dao\model` , the FIRST attribute must be the __'id'__.
2. Do the mapping of the Class in `hibernate.cfg.xml`.
3. Set the entry of the Application in `src.com.ofppt.presentation.MainApp`
4. Put all your class in the __menu__ Map in  `src.com.ofppt.presentation.crudgui.CrudGui`
5. Execute the Application `src.com.ofppt.presentation.MainApp`
