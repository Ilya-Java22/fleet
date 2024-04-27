create table if not exists reports
(
    id serial primary key,
    report varchar,
    start_date TIMESTAMP WITHOUT TIME ZONE,
    end_date TIMESTAMP WITHOUT TIME ZONE,
    report_period_unit varchar,
    results JSONB,
    vehicle_id int
    );
