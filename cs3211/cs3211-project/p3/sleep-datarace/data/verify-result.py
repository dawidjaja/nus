# Result verifier:
# - Count the number of content
# - Count the number of index
# - Check if the url in content and in index are the same
# - Check duplicates

from os import listdir
from os.path import isfile, join

content_path = join('.', 'result', 'content')
index_path = join('.', 'result', 'index')

content = []
for d in listdir(content_path):
  path = join(content_path, d)
  if isfile(path): continue
  for f in listdir(path): 
    content.append(d + '/' + f)

index = []
index_pair = []
for f in listdir(index_path):
  path = join(index_path, f)
  if isfile(path): 
    with open(path, 'r') as index_file: 
      for line in index_file.readlines():
        url = line.split(' ')[0]
        url_path = line.split(' ')[1]
        url_slug = url_path.split('/')[-2].strip() + '/' + url_path.split('/')[-1].strip()
        index.append(url_slug)  
        index_pair.append((url_slug, url))
      

print(f"content number: {len(content)}")
print(f"index   number: {len(index)}")

content = sorted(content)
index = sorted(index)

is_matched = True
for i in range(len(content)):
  s1 = content[i]
  s2 = index[i]
  if (s1 != s2):
    is_matched = False
    print(f"unmatched:\n  `{s1}`\n  `{s2}`")

print(f"URL match: {is_matched}")

def is_there_duplicate(a):
  set_a = set({})
  for v in a:
    set_a.add(v)
  return len(set_a) != len(a)
  
print(f"There is duplicate in index: {is_there_duplicate(index)}")
print(f"There is duplicate in content: {is_there_duplicate(content)}")

if is_there_duplicate(index):
  duplicates = {}
  for k, v in index_pair:
    if k in duplicates: 
      duplicates[k].append(v)
    else:
      duplicates[k] = [v]

  print("Duplicates: ")
  for k in duplicates:
    if len(duplicates[k]) > 1:
      print(f"slug: {k}")
      print('      ' + '\n      '.join(duplicates[k]) + '\n')
