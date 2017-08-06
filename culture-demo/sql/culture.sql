create table comment_info
(
	id int auto_increment
		primary key,
	user int not null,
	work int not null,
	comment varchar(255) not null,
	comment_time timestamp null
)
;

create index comment_info_user_info_FK
	on comment_info (user)
;

create index comment_info_work_info_FK
	on comment_info (work)
;

create table like_info
(
	id int auto_increment
		primary key,
	user int not null,
	work int not null,
	create_time timestamp null,
	type int(4) not null comment '操作类型，1: 点赞， 2:收藏'
)
;

create index like_info_user_info_FK
	on like_info (user)
;

create index like_info_work_info_FK
	on like_info (work)
;

create table user_info
(
	id int auto_increment
		primary key,
	phone varchar(32) not null,
	pwd varchar(32) null,
	alias_name varchar(32) null,
	description varchar(255) null,
	register_time timestamp null,
	profile_path varchar(255) null
)
;

alter table comment_info
	add constraint comment_info_user_info_FK
		foreign key (user) references user_info (id)
			on update cascade on delete cascade
;

alter table like_info
	add constraint like_info_user_info_FK
		foreign key (user) references user_info (id)
			on update cascade on delete cascade
;

create table work_info
(
	id int auto_increment
		primary key,
	work_name varchar(100) not null,
	upload_time timestamp null,
	work_path varchar(255) null,
	author int null,
	work_description varchar(100) null,
	constraint work_info_user_info_FK
		foreign key (author) references user_info (id)
			on update cascade on delete cascade
)
;

create index work_info_user_info_FK
	on work_info (author)
;

alter table comment_info
	add constraint comment_info_work_info_FK
		foreign key (work) references work_info (id)
			on update cascade on delete cascade
;

alter table like_info
	add constraint like_info_work_info_FK
		foreign key (work) references work_info (id)
			on update cascade on delete cascade
;

