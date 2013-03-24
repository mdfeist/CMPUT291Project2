sort -k2 -t'-' data/terms.txt -o data/terms.txt;
sort -t: data/pdates.txt -o data/pdates.txt;
sort -n -t: data/prices.txt -o data/prices.txt;
sort -n -t: data/ads.txt -o data/ads.txt;

awk -F: '{print $1; print $2}' < data/ads.txt | db_load -T -t hash data/ad.idx;
awk -F: '{print $1; print $2}' < data/pdates.txt | db_load -T -c duplicates=1 -t btree data/da.idx;
awk -F: '{print $1; print $2}' < data/prices.txt | db_load -T -c duplicates=1 -t btree data/pr.idx;
awk -F: '{print $1; print $2}' < data/terms.txt | db_load -T -c duplicates=1 -t btree data/te.idx;
