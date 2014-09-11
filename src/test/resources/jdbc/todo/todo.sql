drop table if exists todo;
create table todo ( 
	id bigint not null, 
	description varchar(255) not null, 
	summary varchar(255) not null,
	primary key (id)
);
