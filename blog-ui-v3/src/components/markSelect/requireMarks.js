let marks = []
const modules = import.meta.glob('./../../assets/mark/*.png');
for (const path in modules) {
  const p = {
    path: path,
    name: path.split('assets/mark/')[1]
  };
  marks.push(p);
}


export default marks