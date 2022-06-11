#!/bin/bash
echo "creating db named ... "$USER"_DB"
createdb -h localhost -p $PGPORT $USER"_DB"
pg_ctl status

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
psql -h localhost -p $PGPORT $USER"_DB" < $DIR/../src/create_tables.sql
psql -h localhost -p $PGPORT $USER"_DB" < $DIR/../src/create_indexes.sql
psql -h localhost -p $PGPORT $USER"_DB" < $DIR/../src/create_triggers.sql


echo "Loading data from local computer. MAKE SURE TO ADD YOUR LOCAL PATH TO sql/src/load_data.sql OR THIS WILL NOT WORK. If you have done so, you can ignore the errors following."
psql -h localhost -p $PGPORT $USER"_DB" < $DIR/../src/load_data.sql

echo "done."

