insert into users_authorities (user_id, authority_id)
values ((select id from users where username = 'Василий'), 2);

insert into users_authorities (user_id, authority_id)
values ((select id from users where username = 'Степан'), 2);

insert into users_authorities (user_id, authority_id)
values ((select id from users where username = 'Петр'), 5);

insert into users_authorities (user_id, authority_id)
values ((select id from users where username = 'Илья'), 6);