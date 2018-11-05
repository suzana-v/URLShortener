create table UserAccount (
  accountId varchar unique,
  password varchar
);

create table Url (
  id int auto_increment primary key,
  url varchar unique
);

create table RedirectSettings (
  urlId int,
  redirectType int,
  code varchar unique,
  foreign key(urlId) references Url(id)
);
alter table RedirectSettings add constraint urlId_redirectType_unique unique(urlId, redirectType);

create table RedirectStatistics (
  urlId int unique,
  redirectCount int,
  foreign key(urlId) references Url(id)
);