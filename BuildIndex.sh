sort -u data/terms.txt -o data/terms.txt;
sort -t: data/pdates.txt -o data/pdates.txt;
sort -n -t: data/prices.txt -o data/prices.txt;
sort -u -n -t: data/ads.txt -o data/ads.txt;

awk -F: '{print $1; print $2}' < data/ads.txt | sed 's/\\//g' | db_load -T -t hash data/ad.idx;
awk -F: '{print $1; print $2}' < data/pdates.txt | sed 's/\\//g' | db_load -T -c duplicates=1 -t btree data/da.idx;
awk -F: '{print $1; print $2}' < data/prices.txt | sed 's/\\//g' | db_load -T -c duplicates=1 -t btree data/pr.idx;
awk -F: '{print $1; print $2}' < data/terms.txt | sed 's/\\//g' | db_load -T -c duplicates=1 -t btree data/te.idx;
