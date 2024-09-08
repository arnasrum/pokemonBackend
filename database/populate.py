import psycopg
import json


with psycopg.connect("dbname=pokemon user=backend password=123qwe") as connection:
    with connection.cursor() as cur:
        res = cur.execute("""
            SELECT *
            FROM pokemon;
        """)
        print(cur.fetchone())