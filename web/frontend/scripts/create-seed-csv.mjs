import { readFileSync, writeFileSync } from 'node:fs';
import { fileURLToPath } from 'node:url';
import { dirname, resolve } from 'node:path';

const here = dirname(fileURLToPath(import.meta.url));
const appSource = readFileSync(resolve(here, '../src/App.jsx'), 'utf8');
const marker = 'const seed = ';
const start = appSource.indexOf(marker);
if (start < 0) throw new Error('Could not locate the sample data in App.jsx');
const objectStart = appSource.indexOf('{', start + marker.length);
const objectEnd = appSource.indexOf('\n};', objectStart);
if (objectEnd < 0) throw new Error('Could not locate the end of sample data');
const seed = Function('return (' + appSource.slice(objectStart, objectEnd + 2) + ')')();
const escapeCsv = value => '"' + String(value ?? '').replaceAll('"', '""') + '"';
const rows = [['recordType', 'id', 'dataJson']];
for (const [recordType, records] of Object.entries(seed)) {
  for (const record of records) rows.push([recordType, record.id ?? '', JSON.stringify(record)]);
}
const csv = rows.map(row => row.map(escapeCsv).join(',')).join('\r\n') + '\r\n';
writeFileSync(resolve(here, '../public/foodchain-records.csv'), '\ufeff' + csv, 'utf8');
console.log('Wrote ' + (rows.length - 1) + ' starter records.');

