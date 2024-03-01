insert into authorities (authority) values ('MANAGER');
insert into authorities (authority) values ('USER');
insert into authorities (authority) values ('ADMIN');

insert into users (username, password)
values ('Василий', '$2a$10$48y27jMa4/PPxXHHvTQ8M.p5zzSmOEGGzsHKUZMH6HGtaqDzxgUVS');

insert into users (username, password)
values ('Степан', '$2a$10$48y27jMa4/PPxXHHvTQ8M.p5zzSmOEGGzsHKUZMH6HGtaqDzxgUVS');

insert into users (username, password)
values ('Петр', '$2a$10$aqY7gw2vSqjtahb/tzh6cOUM438vmVLBTA929dTcy9hA5lcaZRvQy');

insert into users (username, password)
values ('Илья', '$2a$10$1zqe0fLT9aJxEa4bliDCyefTURcXiW0hYd8ov1NcoSquv34YTenaG');

insert into users_enterprises (user_id, enterprise_id)
values ((select id from users where username = 'Василий'), 1);

insert into users_enterprises (user_id, enterprise_id)
values ((select id from users where username = 'Василий'), 2);

insert into users_enterprises (user_id, enterprise_id)
values ((select id from users where username = 'Степан'), 2);

insert into users_enterprises (user_id, enterprise_id)
values ((select id from users where username = 'Степан'), 3);

insert into users_enterprises (user_id, enterprise_id)
values ((select id from users where username = 'Илья'), 1);

insert into users_enterprises (user_id, enterprise_id)
values ((select id from users where username = 'Илья'), 2);

insert into users_enterprises (user_id, enterprise_id)
values ((select id from users where username = 'Илья'), 3);