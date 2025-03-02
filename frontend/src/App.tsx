import { Route, Routes } from "react-router";
import { FullLayout } from "./layout/FullLayout";
import { HomePage } from "./Home/HomePage";

export function App() {
  return (
    <FullLayout>
      <Routes>
        <Route path="/" element={<HomePage />} />
      </Routes>
    </FullLayout>
  );
}
