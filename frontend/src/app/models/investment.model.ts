export interface Investment {
  idInvestment: number;
  name: string;
  type: InvestmentType;
  amount: number;
  interestRate: number;
  startDate: Date;
  endDate: Date;
  status: InvestmentStatus;
}

export enum InvestmentType {
  FUND = "FUND",
  GOLD = "GOLD",
  SILVER = "SILVER"
}

export enum InvestmentStatus {
  ACTIVE = "ACTIVE",
  INACTIVE = "INACTIVE"
}