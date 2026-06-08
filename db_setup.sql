do $$
begin
    if not exists (select from pg_roles where rolname = 'labadmin') then
        create role labadmin with login password 'labpass';
    end if;
end
$$;

create database labafive owner labadmin;
