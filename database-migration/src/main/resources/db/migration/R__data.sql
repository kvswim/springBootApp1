delete from reports;
delete from reports_aud;
delete from indicators;
delete from ind_types;
delete from link_reports_indicators;

insert into reports(id, display_name, created_date, reviewed) values(1, 'BadGuys.txt', '2018-11-01', true);
insert into reports(id, display_name, created_date, reviewed) values(2, 'Domains.txt', '2019-01-02', false);
insert into reports(id, display_name, created_date, reviewed) values(3, 'AdamsStuff.pdf', '2019-01-02', true);
insert into reports(id, display_name, created_date, reviewed) values(4, 'KevinsStuff.txt', '2019-02-04', true);
insert into reports(id, display_name, created_date, reviewed) values(5, 'KatsIPs.pdf', '2019-03-13', false);

insert into indicators(id, value, ind_type) values(11, 'badguy.com', 1);
insert into indicators(id, value, ind_type) values(12, 'google.com', 1);
insert into indicators(id, value, ind_type) values(13, 'yahoo.com', 1);
insert into indicators(id, value, ind_type) values(14, '127.0.0.1', 2);
insert into indicators(id, value, ind_type) values(15, '128.0.0.1', 2);
insert into indicators(id, value, ind_type) values(16, 'bing.com', 1);

insert into ind_types(id, value) values(1, 'domain');
insert into ind_types(id, value) values(2, 'ip');

insert into link_reports_indicators(id, report, indicator) values(21, 1, 11);
insert into link_reports_indicators(id, report, indicator) values(22, 2, 12);
insert into link_reports_indicators(id, report, indicator) values(23, 2, 13);
insert into link_reports_indicators(id, report, indicator) values(24, 4, 14);
insert into link_reports_indicators(id, report, indicator) values(25, 4, 15);
insert into link_reports_indicators(id, report, indicator) values(26, 4, 16);
insert into link_reports_indicators(id, report, indicator) values(27, 5, 14);
insert into link_reports_indicators(id, report, indicator) values(28, 5, 15);