console.log("WoHinDamit? JavaScript läuft!");

const itemInput = document.getElementById("itemInput");
const searchButton = document.getElementById("searchButton");
const result = document.getElementById("result");
const condition = document.getElementById("condition");
const repairBox = document.getElementById("repair-box");
const repairable = document.getElementById("repairable");

function searchItem() {
  const item = itemInput.value.trim().toLowerCase();
  const zustand = condition.value;
  const reparatur = repairable.value;

  console.log("Zustand:", zustand);

  console.log("Gesuchter Gegenstand:", item);

  const geraete = {
    handy: {
      name: "Handy",
      kategorie: " Elektrogerät ",
      entsorgungsart: " Elektrogeräte-Sammlung ",
    },
    laptop: {
      name: "Laptop",
      kategorie: " Elektrogerät ",
      entsorgungsart: " Elektrogeräte-Sammlung ",
    },
    tablet: {
      name: "Tablet",
      kategorie: " Elektrogerät ",
      entsorgungsart: " Elektrogeräte-Sammlung ",
    },
    flasche: {
      name: "Flasche",
      kategorie: " Verpackung ",
      entsorgungsart: "Material prüfen",
    },
  };

  const geraet = geraete[item]?.name;
  const kategorie = geraete[item]?.kategorie?.trim();
  console.log("Kategorie:", JSON.stringify(kategorie));
  console.log("Ist Elektrogerät?", kategorie === "Elektrogerät");
  console.log("Entsorgungsart:", geraete[item]?.entsorgungsart);
  const erkannt = Object.hasOwn(geraete, item);
  console.log("Gegenstand erkannt:", erkannt);
  if (!erkannt) {
    result.textContent = "Gegenstand noch nicht erkannt.";
    return;
  }

  if (kategorie === "Elektrogerät") {
    if (zustand === "ja") {
      result.textContent =
        "Das " +
        geraet +
        " funktioniert noch. " +
        " Empfehlung: Wiederverwenden oder Spenden.";
    } else if (reparatur === "ja") {
      result.textContent =
        "Das " +
        geraet +
        " ist defekt, aber reparierbar. " +
        " Empfehlung: Reparatur prüfen.";
    } else {
      result.textContent =
        "Das " +
        geraet +
        " ist nicht reparierbar. " +
        " Empfehlung: Zur Elektroaltgeräte-Sammelstelle bringen.";
    }
  } else if (item === "flasche") {
    result.textContent = "Falsche erkannt - Recycling prüfen.";
  } else {
    result.textContent = "Gegenstand noch nicht erkannt.";
  }

  if (kategorie) {
    result.textContent += " Kategorie: " + kategorie;
  }

  const entsorgungsart = geraete[item]?.entsorgungsart;

  if (
    entsorgungsart &&
    (kategorie !== "Elektrogerät" ||
      (zustand === "nein" && reparatur === "nein"))
  ) {
    result.textContent += " Entsorgungsart: " + entsorgungsart;
  }
}

searchButton.addEventListener("click", searchItem);

condition.addEventListener("change", searchItem);

repairable.addEventListener("change", searchItem);

function updateRepairBox() {
  if (condition.value === "nein") {
    repairBox.style.display = "block";
  } else {
    repairBox.style.display = "none";
  }
}

condition.addEventListener("change", updateRepairBox);

updateRepairBox();

const sammelstellen = [
  {
    name: "Mistplatz Beispiel",
    bezirk: "Wien",
    typ: "Elektrogeräte",
  },
  {
    name: "Recyclingstelle Beispiel",
    bezirk: "Wien",
    typ: "Verpackung",
  },
  {
    name: "Elektro Sammelstelle Beispiel",
    bezirk: "Wien",
    typ: "Elektrogeräte",
  },
];

console.log("Sammelstellen:", sammelstellen);
console.log("Name", sammelstellen[0].name);
console.log("Zweite Sammelstelle:", sammelstellen[1].name);

if (sammelstellen[0].typ === "Elektrogeräte") {
  console.log("Elektrogeräte können hier abgegeben werden.");
}

const passendeStelle = sammelstellen.find(
  (stelle) => stelle.typ === "Elektrogeräte",
);

const passendeStellen = sammelstellen.filter(
  (stelle) => stelle.typ === "Elektrogeräte",
);

console.log("Alle passenden Sammelstellen:", passendeStellen);
console.log("Gefundene Sammelstelle:", passendeStelle);
console.log("Anzahl:", passendeStellen.length);

const ersteStelle = sammelstellen.find(
  (stelle) => stelle.typ === "Elektrogeräte",
);

const alleStellen = sammelstellen.filter(
  (stelle) => stelle.typ === "Elektrogeräte",
);

console.log("Erste Stelle:", ersteStelle.name);
console.log("Alle Stellen:", alleStellen.length);

alleStellen.forEach((stelle) => {
  console.log("Sammelstelle:", stelle.name);
});

const sammelstellenListe = document.getElementById("sammelstellenListe");

function zeigeSammelstellen(typ) {
  sammelstellenListe.textContent = "";

  const passende = sammelstellen.filter((stelle) => stelle.typ === typ);

  passende.forEach((stelle) => {
    const eintrag = document.createElement("p");
    eintrag.textContent = stelle.name;
    sammelstellenListe.appendChild(eintrag);
  });
}

zeigeSammelstellen("Elektrogeräte");
