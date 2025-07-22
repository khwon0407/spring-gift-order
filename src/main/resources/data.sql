insert into products(name, price, image_url)
VALUES ('아메리카노', 4500, 'http://image.url.americano');
insert into products(name, price, image_url)
VALUES ('에스프레소', 2500, 'http://image.url.espresso');
insert into products(name, price, image_url)
VALUES ('라떼', 5000, 'http://image.url.latte');
insert into products(name, price, image_url)
VALUES ('카푸치노', 5500, 'http://image.url.capuchino');

insert into members(email, password, role)
values ('admin@admin.com', 'adminpw', 'ADMIN');
insert into members(email, password, role)
values ('user@user.com', 'userpw', 'USER');

insert into product_options(name, quantity, product_id)
values ('기본 옵션', 100, 1);
insert into product_options(name, quantity, product_id)
values ('기본 옵션2', 200, 2);
insert into product_options(name, quantity, product_id)
values ('기본 옵션3', 300, 3);
insert into product_options(name, quantity, product_id)
values ('기본 옵션4', 400, 4);