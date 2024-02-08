insert into authorities (authority) values ('MANAGER');

insert into managers (username, password, authority_id)
values ('Василий', '$2a$10$48y27jMa4/PPxXHHvTQ8M.p5zzSmOEGGzsHKUZMH6HGtaqDzxgUVS',
(select id from authorities where authority = 'MANAGER'));

insert into managers (username, password, authority_id)
values ('Степан', '$2a$10$48y27jMa4/PPxXHHvTQ8M.p5zzSmOEGGzsHKUZMH6HGtaqDzxgUVS',
(select id from authorities where authority = 'MANAGER'));

insert into managers_enterprises (manager_id, enterprise_id)
values ((select id from managers where username = 'Василий'), 1);

insert into managers_enterprises (manager_id, enterprise_id)
values ((select id from managers where username = 'Василий'), 2);

insert into managers_enterprises (manager_id, enterprise_id)
values ((select id from managers where username = 'Степан'), 2);

insert into managers_enterprises (manager_id, enterprise_id)
values ((select id from managers where username = 'Степан'), 3);
