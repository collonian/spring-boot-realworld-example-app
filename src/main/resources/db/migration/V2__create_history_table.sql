
create table article_history (
  id varchar(255) primary key,
  article_id varchar(255) not null,
  history_code varchar(255) not null,
  title varchar(255),
  description text,
  body text,
  created_at TIMESTAMP NOT NULL
);
