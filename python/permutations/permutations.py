# Use Heap's Algorithm to generate all permutations of a list:
# https://en.wikipedia.org/wiki/Heap%27s_algorithm

# procedure generate(k : integer, A : array of any):
#     if k = 1 then
#         output(A)
#     else
#         // Generate permutations with k-th unaltered
#         // Initially k = length(A)
#         generate(k - 1, A)

#         // Generate permutations for k-th swapped with each k-1 initial
#         for i := 0; i < k-1; i += 1 do
#             // Swap choice dependent on parity of k (even or odd)
#             if k is even then
#                 swap(A[i], A[k-1]) // zero-indexed, the k-th is at k-1
#             else
#                 swap(A[0], A[k-1])
#             end if
#             generate(k - 1, A)
#         end for
#     end if

plist = list()
permutations = set()
totals = dict()

def generate(k: int, A:list):
    if k == 1:
        # print(A)
        plist.append(A)
        permutations.add(tuple(A))
    else:
        generate(k-1, A)
        for i in range(0, k-1):
            if k % 2 == 0:
                tmp = A[i]
                A[i] = A[k-1]
                A[k-1] = tmp
            else:
                tmp = A[0]
                A[0] = A[k-1]
                A[k-1] = tmp
            
            generate(k - 1, A)

def get_totals():
    for perm in permutations:
        a = sum(perm[0:3])
        b = sum(perm[3:6])
        c = sum(perm[6:9])
        d = perm[0]+ perm[3]+ perm[6]
        e = perm[1]+ perm[4]+ perm[7]
        f = perm[2]+ perm[5]+ perm[8]

        t = [a, b, c, d, e, f]
        t = sorted(t, reverse = True)
        # t.append(perm)
        # totals.add(tuple(t))
        totals[tuple(t)] = perm

A = [6, 5, 5, 4, 3, 2, 2, 2, 1]
generate(len(A), A)

get_totals()

keys = [k for k in totals]
keys = sorted(keys, reverse=True)

print(f"Total permutations: {len(plist)}")
print(f"Unique permutations: {len(permutations)}")
print(f"Unique totals, sorted ({len(totals)})")
# print('\n'.join(str(s) for s in totals))
# print('\n'.join(f"{str(k)} {str([v])}" for k in totals))
keys = [key for key in keys if len([i for i in key if i >= 10])>= 4]

for key in keys:

    for k, v in totals.items():
        if key == k:
            print(f"{k}\t\t{v}")
            # print(sum(key))
            pass