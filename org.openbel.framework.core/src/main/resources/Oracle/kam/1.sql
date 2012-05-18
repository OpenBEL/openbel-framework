begin
for i in (select constraint_name, table_name from all_constraints where owner = '@' and status = 'ENABLED') LOOP
execute immediate 'alter table @.'||i.table_name||' disable constraint '||i.constraint_name||'';
end loop;
end;
##
begin
for i in (select table_name from all_tables where owner = '@') LOOP
execute immediate 'truncate table @.'||i.table_name||'';
end loop;
end;
##
begin
for i in (select constraint_name, table_name from all_constraints where owner = '@' and status = 'DISABLED') LOOP
execute immediate 'alter table @.'||i.table_name||' enable constraint '||i.constraint_name||'';
end loop;
end;
##