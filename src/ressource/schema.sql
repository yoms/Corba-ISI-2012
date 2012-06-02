create table if not exists avatar (
	id 				integer primary key,
	pseudo 			varchar(45) not null unique,
	code_acces 		varchar(45) not null,
	taille 			varchar(45) not null,
	humeur 			varchar(45) not null,
	sexe 			varchar(45) not null,
	id_piece 		int null,
	est_admin 		tinyint(1) null default false,
	est_connecte 	tinyint(1) null default false,
	constraint fk_id_piece foreign key (id_piece) references piece (id)
);

create table if not exists post (
	id 				integer primary key,
	pseudoEmetteur 	varchar(45) not null,
	contenu 		varchar(45) null,
	date_heure 		datetime null,
	id_avatar 		int null,
	constraint fk_id_poster foreign key (id_avatar) references avatar (id)
);

create table if not exists piece (
	id 			integer primary key,
	posX	 	int null,
	posY 		int null,
	nom 		varchar(45) null,
	id_nord 	int null,
	id_sud 		int null,
	id_est 		int null,
	id_ouest 	int null,
	constraint fk_id_nord 	foreign key (id_nord) 	references piece (id),
	constraint fk_id_sud 	foreign key (id_sud) 	references piece (id),
	constraint fk_id_est 	foreign key (id_est) 	references piece (id),
	constraint fk_id_ouest 	foreign key (id_ouest) 	references piece (id)
);
