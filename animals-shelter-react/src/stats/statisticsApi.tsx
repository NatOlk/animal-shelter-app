export async function fetchAnimalCount(): Promise<number> {
  const response = await fetch("/ansh/stats/stats/animals/count")
  if (!response.ok) throw new Error("Failed to fetch animal count")
  return response.json()
}

export async function fetchVaccinationCount(): Promise<number> {
  const response = await fetch("/ansh/stats/stats/vaccinations/count")
  if (!response.ok) throw new Error("Failed to fetch vaccination count")
  return response.json()
}

export async function fetchAnimalsByDate(): Promise<{ [date: string]: number }> {
  const response = await fetch("/ansh/stats/stats/animals/added-by-date")
  if (!response.ok) throw new Error("Failed to fetch animal stats by date")
  return response.json()
}

export async function fetchVaccinationsByDate(): Promise<{ [date: string]: number }> {
  const response = await fetch("/ansh/stats/stats/vaccinations/added-by-date")
  if (!response.ok) throw new Error("Failed to fetch vaccination stats by date")
  return response.json()
}

export async function fetchAnimalLifespans(): Promise<{ [animalId: string]: number }> {
  const response = await fetch("/ansh/stats/stats/animals/lifespans")
  if (!response.ok) throw new Error("Failed to fetch animal lifespans")
  return response.json()
}
