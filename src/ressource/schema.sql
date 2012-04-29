create table if not exists avatar (
  id integer primary key,

  pseudo varchar(45) not null unique,

  code_acces varchar(45) not null ,

  taille varchar(45) not null ,

  humeur varchar(45) not null ,

  sexe varchar(45) not null ,

  piece_courante varchar(45) null ,

  est_admin tinyint(1) null default false ,

  est_connecte tinyint(1) null default false
   );
  
create table if not exists post (
  id integer primary key,

  contenu varchar(45) null ,

  date_heure datetime null ,

  id_posteur int null ,

  constraint fk_id_poster

    foreign key (id_posteur )

    references avatar (id ));
    
    